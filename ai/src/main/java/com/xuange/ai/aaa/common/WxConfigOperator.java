package com.xuange.ai.aaa.common;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
//xuange
@Component
public class WxConfigOperator {
    @Autowired
    private WxConfigProperties wxConfigProperties;

    @Bean
    public WxMaService WxMaService() {
        WxMaServiceImpl wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl wxMaDefaultConfig = new WxMaDefaultConfigImpl();
        wxMaDefaultConfig.setAppid(wxConfigProperties.getAppId());
        wxMaDefaultConfig.setSecret(wxConfigProperties.getSecret());
        wxMaService.setWxMaConfig(wxMaDefaultConfig);
        return wxMaService;
    }

}
