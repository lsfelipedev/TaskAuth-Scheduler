package com.Notification.security;

import com.Notification.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secretKey;


    public String generate(User user){

        try{
            var algorithm = Algorithm.HMAC256(secretKey);

            String token = JWT.create()
                    .withIssuer("LuisFelipe")
                    .withSubject(user.getLogin())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        }
        catch(JWTCreationException e){
            throw new RuntimeException("Error while generation token", e);
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("LuisFelipe")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch(JWTVerificationException exception){
            return "";
        }
    }
}
