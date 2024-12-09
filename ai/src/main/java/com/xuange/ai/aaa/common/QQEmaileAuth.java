package com.xuange.ai.aaa.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//xuange
@Component
public class QQEmaileAuth {
    @Value("${QQEmaileauth}")
    public static String qqEmaileAuth;
}
