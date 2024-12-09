package com.xuange.ai.aaa.controller;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xuange.ai.aaa.common.*;
import com.xuange.ai.aaa.transaction.Client1;
import jakarta.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.ZipOutputStream;

import static com.xuange.ai.AiApplication.*;
import static com.xuange.ai.aaa.common.Tread.getTreadPool;
//xuange
@RestController
@CrossOrigin
@Component
@RequestMapping("ChatGetPhone")
public class PhoneLogin {
    @Autowired
    public RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private HttpServletRequest httpServletRequest;
    public static Map<String,Client1> phoneMap =new HashMap<>();
    ThreadPoolExecutor myTreadPool = getTreadPool
            (10, 20, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

    @PostMapping("/send")
    public Boolean send(@RequestBody String phone)  {
        String[] split = phone.split(":");

        String[] split1 = split[1].split("}");
        String phon =split1[0].replaceAll("\"","");
        String sixBitRandom = RandomUtil.getSixBitRandom();
        if (StringUtils.isEmpty(phon)) {
            return false;
        }


        //整合阿里云短信服务
        //设置相关参数
        DefaultProfile profile = DefaultProfile.
                getProfile(CompontProperty.REGION_ID,
                        CompontProperty.ACCESS_KEY_ID,
                        CompontProperty.SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        //手机号
        request.putQueryParameter("PhoneNumbers", phon);
        //签名名称
        request.putQueryParameter("SignName", "学习中小学生劳动教育平台");
        //模板code
        request.putQueryParameter("TemplateCode", "SMS_464821440");
        //验证码  使用json格式   {"code":"123456"}
        Map<String, Object> param = new HashMap<>();
        param.put("code", sixBitRandom);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));


        //调用方法进行短信发送

        FutureTask<Boolean> futureTask = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    CommonResponse response = client.getCommonResponse(request);

                    return response.getHttpResponse().isSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });


        try {
            myTreadPool.execute(futureTask);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Boolean b = futureTask.get();
            if (b){
                CompletableFuture.runAsync(() -> {
                    String token = JwtHelper.createToken(phon);


                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("code",sixBitRandom);
                    hashmap.put("token",token);
                    redisTemplate.opsForHash().putAll(phon,hashmap);
                    Object o = redisTemplate.opsForHash().get(phon, "code");


                    redisTemplate.expire(phon, 1, TimeUnit.HOURS);
                },myTreadPool);



            }
            return b;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/UserLogin")
    public String Login(@RequestBody String message){
        String[] split = message.split("\"");
        String phone = split[3];
        String code = split[7];
        String o = (String) redisTemplate.opsForHash().get(phone, "code");
        if(o!=null|| Objects.equals(o, code)){
            CompletableFuture.runAsync(()->{
                Client1 client1 = new Client1();
                FromMessageTo fromMessageTo = new FromMessageTo(phone, phone + "已经登录", "server");
                client1.startClient(10088,param1List.getFirst(),phone);
                phoneMap.put(phone,client1);
            });

            return (String) redisTemplate.opsForHash().get(phone,"token");
        }
        return "code error";
    }
    @PostMapping("/getMessage")
    public List<FromMessage> getMessage(){
        String token = httpServletRequest.getHeader("Token");
        if(token==null|| token.equals("")){
            FromMessage fromMessage = new FromMessage(1,"server", "token失效请重新登录");
            ArrayList<FromMessage> fromMessagesList = new ArrayList<>();
            fromMessagesList.add(fromMessage);
            return fromMessagesList;
        }
        String phone = JwtHelper.getUserId(token);
        Client1 client1 = phoneMap.get(phone);

        if(client1 == null){
            FromMessage message = new FromMessage(2,"server", "会话已经关闭。请重新链接");
            ArrayList<FromMessage> objects = new ArrayList<>();
            objects.add(message);
            return objects;
        }
        return   client1.clientmessageListOfIn;

    }
    @PostMapping("/sendMessage")
    public boolean sendMessage(@RequestBody String s){
        String[] split = s.split("\"");
        String mes = split[3];
        String tophone = split[7];
        String token = httpServletRequest.getHeader("Token");

        String phone = JwtHelper.getUserId(token);
        Client1 client1 = phoneMap.get(phone);
        if(client1 == null){
            return false;
        }
        FromMessageTo fromMessageTo = new FromMessageTo(phone, mes, tophone);
        boolean result = client1.clientmessageList.add(fromMessageTo);
        return result;

    }
    @PostMapping("/removeItems")
    public boolean removeItems(@RequestBody String s){
        JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
        Long keyId = jsonObject.get("keyId").getAsLong();
        System.out.println(keyId);

        String token = httpServletRequest.getHeader("Token");
        String phone = JwtHelper.getUserId(token);
        Client1 client1 = phoneMap.get(phone);
        if(client1 == null){
            return false;
        }

        return client1.removeByKeyId(keyId);
    }
    @PostMapping("/logout")
    public Boolean logout(){
        String token = httpServletRequest.getHeader("Token");
        String userId = JwtHelper.getUserId(token);
        if (userId != null) {
            redisTemplate.opsForHash().delete(userId);
            return true;
        }
        return false;
    }
    @PostMapping("applyForFriends")
    public void  applyForFriends(@RequestBody String s){
        String token = httpServletRequest.getHeader("Token");
        String fromPhone = JwtHelper.getUserId(token);
        JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
        String applyPhone = jsonObject.get("applyPhone").getAsString();
        if(need.containsKey(applyPhone)){
            List<String> strings = need.get(applyPhone);
            strings.add(fromPhone);
            need.replace(applyPhone,strings);
        }else {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(fromPhone);
            need.put(applyPhone,strings);
        }


    }
    @PostMapping("getForApplyFriend")
    public List<String> getForApplyFriend(){
        String token = httpServletRequest.getHeader("Token");
        String Phone = JwtHelper.getUserId(token);
        return need.get(Phone);
    }

    @PostMapping("Agree")
    public void agree(@RequestBody String s ){
        JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
        int keyId = jsonObject.get("keyId").getAsInt();
        String token = httpServletRequest.getHeader("Token");
        String userId = JwtHelper.getUserId(token);
        List<String> strings1 = need.get(userId);
        if(friendList.containsKey(userId)){

            List<String> strings = friendList.get(userId);
            strings.add(strings1.get(keyId));
            friendList.replace(userId,strings);
        }else {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(strings1.get(keyId));
            friendList.put(userId,strings);
        }
        strings1.remove(userId);

    }
    @PostMapping("Photo")
    public void photo(@RequestBody MultipartFile file){
        String token = httpServletRequest.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        try {
            FileOutputStream indexOutPutStream = new FileOutputStream("/home/xuange/ss/index.txt",true);
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String name = file.getName();
            FileOutputStream fileOutputStream = new FileOutputStream("/home/xuange/ss"+originalFilename+name);
            String indexOfRow =userId+":"+"/home/xuange/ss"+originalFilename+name;
            indexOutPutStream.write(indexOfRow.getBytes(StandardCharsets.UTF_8));

            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            zipOutputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
//