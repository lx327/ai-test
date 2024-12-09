package com.xuange.ai.aaa.common;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
//xuange
@Component
@Aspect  //切面类
public class EzceptionAspect {

    @Autowired
    private RedisTemplate redisTemplate;


    @Pointcut("@annotation(com.xuange.ai.aaa.common.MyLog)")
    public void serviceMethods() {
    }

    //环绕通知，登录判断
    //切入点表达式：指定对哪些规则的方法进行增强

    @Around("serviceMethods()")
    public Object Exception(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return null;
            }
            HttpServletRequest request = attributes.getRequest();
            String ip = request.getRemoteHost();
            Object[] args = proceedingJoinPoint.getArgs();
            Class<? extends ProceedingJoinPoint> aClass = proceedingJoinPoint.getClass();
            String kind = proceedingJoinPoint.getKind();
            Signature signature = proceedingJoinPoint.getSignature();
            SourceLocation sourceLocation = proceedingJoinPoint.getSourceLocation();
            JoinPoint.StaticPart staticPart = proceedingJoinPoint.getStaticPart();
            Object target = proceedingJoinPoint.getTarget();
            Object aThis = proceedingJoinPoint.getThis();
            long time = System.currentTimeMillis();

            Object proceed = proceedingJoinPoint.proceed();

        } catch (Exception e) {
        } finally {

        }
         return proceedingJoinPoint.proceed();

    }

}

//execution(* com.xuange.ai.*.*(..))