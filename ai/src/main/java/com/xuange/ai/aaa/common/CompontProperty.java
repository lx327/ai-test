package com.xuange.ai.aaa.common;

//import javafx.fxml.Initializable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//xuange
@Component
public class CompontProperty implements InitializingBean {
    @Value("${aliyun.sms.regionId}")
    private String regionId;
    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.secret}")
    private String secret;
    public static String REGION_ID;
    public static String ACCESS_KEY_ID;
    public static String SECRET;


    @Override
    public void afterPropertiesSet() {
     REGION_ID=regionId;
     ACCESS_KEY_ID=accessKeyId;
     SECRET=secret;
    }
}
//