package com.xuange.ai.aaa.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
//xuange
@Component
@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxConfigProperties {
    private String appId;
    private String secret;
}
