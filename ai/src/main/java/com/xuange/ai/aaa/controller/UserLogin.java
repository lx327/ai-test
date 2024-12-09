package com.xuange.ai.aaa.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuange.ai.aaa.common.WxConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
//xuange
@RestController
@CrossOrigin
@Component
@RequestMapping("login")
public class UserLogin {

    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private WxConfigProperties wxConfigProperties;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @PostMapping("/callback")
    public String save(String code,int state){
        String response = null;
        try {
            String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                   wxConfigProperties.getAppId() , wxConfigProperties.getSecret(), code);

            RestTemplate restTemplate = new RestTemplate();
             response = restTemplate.getForObject(url, String.class);

            // 解析响应，提取 openid 和 session_key
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            String openid = jsonNode.get("openid").asText();
            String sessionKey = jsonNode.get("session_key").asText();
            String accessToken = jsonNode.get("access_token").asText();
//            String token = JwtHelper.createToken();
            String userInfoUrl = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN",
                    accessToken, openid);
            String userInfoResponse = restTemplate.getForObject(userInfoUrl, String.class);



        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
        return response; // 返回用户信息
    }
}
