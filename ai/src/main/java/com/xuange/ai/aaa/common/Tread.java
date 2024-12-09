package com.xuange.ai.aaa.common;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//xuange
public class Tread {
    public static ThreadPoolExecutor  getTreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, LinkedBlockingQueue<Runnable> workQueue
            , ThreadPoolExecutor.CallerRunsPolicy handler) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        return threadPoolExecutor;
    }

}
