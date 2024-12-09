package com.xuange.ai.aaa.filter;


import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;

import java.util.EnumSet;
//xuange
public class AppInit implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic bloomFilter = servletContext.addFilter("BloomFilter", new BloomFilter());
        bloomFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST,DispatcherType.FORWARD),false,"");
    }
}
