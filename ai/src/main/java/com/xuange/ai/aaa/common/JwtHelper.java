package com.xuange.ai.aaa.common;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;
//xuange
public class JwtHelper {

    //过期时间
    public static long tokenExpiration = 24*60*60*1000;
    //签名秘钥
    private static String tokenSignKey = "541881452";

    //根据参数生成token
    public static String createToken(String userId) {
        Date date = new Date(System.currentTimeMillis() + tokenExpiration);
        String token = Jwts.builder()
                .setSubject("Reservation-USER")
                .setExpiration(date)
                .claim("userId", userId)

                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    //根据token字符串得到用户id
    public static String getUserId(String token) {
        if(StringUtils.isEmpty(token)) return null;

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String o = (String) claims.get("userId");
        return o;
    }

    //根据token字符串得到用户名称
    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)) return "";

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userName");
    }


}