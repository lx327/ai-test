package com.xuange.ai.aaa.common;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
//xuange
@Component
@Aspect  //切面类
public class LoginAspect {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    HttpServletRequest httpServletRequest;

    //环绕通知，登录判断
    //切入点表达式：指定对哪些规则的方法进行增强
    @Around("execution(* com.xuange.ai.*.*(..)) && @annotation(login)")
    public Object login(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String token = httpServletRequest.getHeader("Token");

        if (StringUtils.hasText(token)) {
            throw new RuntimeException("no login");
        }
        String phoneId = (String) redisTemplate.opsForValue().get(token);
        if (StringUtils.hasText(phoneId)) {
            throw new RuntimeException("no register");
        }
        return proceedingJoinPoint.proceed();
    }

}
