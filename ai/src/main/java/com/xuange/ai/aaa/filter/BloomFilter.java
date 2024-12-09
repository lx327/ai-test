package com.xuange.ai.aaa.filter;


import co.elastic.clients.elasticsearch.security.User;
import com.google.gson.Gson;
import com.xuange.ai.aaa.common.Bloom;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

//xuange
public class BloomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Jedis jedis = new Jedis("localhost", 6379);
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(httpRequest);
        String requestBody1 = wrappedRequest.getRequestBody();
        Gson gson = new Gson();
        User user = gson.fromJson(requestBody1, User.class);


        // 读取请求体
        String requestBody = wrappedRequest.getRequestBody();

        //TODO 获取用户Id值并取hash
        int idHash=3;
        boolean b = Bloom.bitSet.get(idHash);
        if (b){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            String s1 = jedis.get("id");
            if (s1!=null){
                int i = s1.hashCode();
                Bloom.bitSet.set(i);
                filterChain.doFilter(servletRequest,servletResponse);
            }
            else {
                //TODO 过滤掉后的处理逻辑

            }
//            filterChain.doFilter(servletRequest,servletResponse);

        }
    }
}
class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private final String cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        this.cachedBody = stringBuilder.toString();
    }

    public String getRequestBody() {
        return this.cachedBody;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // No-op
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
    }
}
//@Bean   //如果过滤器需要依赖 Spring 容器中的 Bean，可以使用 DelegatingFilterProxy：
//public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
//    FilterRegistrationBean<DelegatingFilterProxy> registrationBean = new FilterRegistrationBean<>();
//    DelegatingFilterProxy proxy = new DelegatingFilterProxy("springBeanFilter");
//    registrationBean.setFilter(proxy);
//    registrationBean.addUrlPatterns("/*");
//    return registrationBean;
//}




//<filter>
//    <filter-name>customFilter</filter-name>
//    <filter-class>com.example.CustomFilter</filter-class>
//</filter>
//<filter-mapping>
//    <filter-name>customFilter</filter-name>
//    <url-pattern>/*</url-pattern>
//</filter-mapping>


//@WebFilter(urlPatterns = "/api/someFunction") // 绑定到某个函数的 URL
// @Bean
//    public FilterRegistrationBean<Filter> bloomFilterRegistration() {
//        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
//
//        // 实例化过滤器
//        registrationBean.setFilter(new BloomFilter());
//
//        // 设置过滤器应用的 URL 模式
//        registrationBean.addUrlPatterns("/api/someFunction");
//
//        // 设置过滤器的顺序（越小越优先）
//        registrationBean.setOrder(1);
//
//        // 可选：为过滤器添加初始化参数
//        registrationBean.addInitParameter("paramName", "paramValue");
//
//        return registrationBean;
//    }
// 还需要在主类中加上 @ServletComponentScan 注解，启用组件扫描