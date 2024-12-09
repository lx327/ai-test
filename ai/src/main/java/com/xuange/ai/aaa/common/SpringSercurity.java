package com.xuange.ai.aaa.common;
//xuange
public class SpringSercurity {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                //禁用csrf(防止跨站请求伪造攻击)
//                .csrf()
//                .disable()
//                // 设置白名单
//                .authorizeHttpRequests()
//                //swg相关
//                .requestMatchers("/favicon.ico","/swagger-resources/**", "/webjars/**", "/v3/**", "/doc.html").permitAll()
//                //用户登录相关接口
//                .requestMatchers("/securityLogin/login").permitAll()
//                // 对于其他任何请求，都保护起来
//                .anyRequest().authenticated()
//                .and()
//                // 禁用缓存
//                .sessionManagement()
//                // 使用无状态session，即不使用session缓存数据
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                // 添加身份验证
//                .and()
//
//                .authenticationProvider(authenticationProvider())
//                // 添加token过滤器
//                .addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class)
//                .addFilter(new TokenLoginFilter(authenticationManager(authenticationConfiguration), redisTemplate, sysLoginLogFeignClient))
//        ;
//
//
//        return http.build();
//    }
}
