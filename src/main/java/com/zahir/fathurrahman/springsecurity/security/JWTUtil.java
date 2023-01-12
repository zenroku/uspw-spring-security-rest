package com.zahir.fathurrahman.springsecurity.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public class JWTUtil {
    // the 'secret' value of string below, if you wanna go to production, please make it from env or prop
    private static final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

    public static String generateAccessToken(HttpServletRequest request, String username, List<String> roles){
        String access_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",roles)
                .sign(algorithm);
        return access_token;
    }

    public static String generateRefreshToken(HttpServletRequest request,String username){
        String refresh_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        return refresh_token;
    }

    public static DecodedJWT decodedJWT(String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }
}
