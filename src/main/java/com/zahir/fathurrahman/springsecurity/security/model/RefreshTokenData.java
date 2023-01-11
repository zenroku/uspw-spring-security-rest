package com.zahir.fathurrahman.springsecurity.security.model;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenData {
    String accessToken;
    String refreshToken;
    String username;
    DecodedJWT decodedJWT;
}
