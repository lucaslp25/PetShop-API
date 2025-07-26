package com.lucasdev.petshop_api.security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.lucasdev.petshop_api.security.exceptions.PetShopSecurityException;
import com.lucasdev.petshop_api.security.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("test")
public class PetShopTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private final String ISSUER = "PETSHOP-API";

    public String generateToken(User user){

        Algorithm algorithm = Algorithm.HMAC256(secret); //we´ll use the (HMAC-256) encryption

        try{
            List<String> roles = user.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = JWT
                    .create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getLogin())
                    .withExpiresAt(petTimesExpires())
                    .withClaim("roles", roles) //here makes the perform so better
                    .sign(algorithm);
            return token;

        }catch(JWTCreationException pet){
            throw new PetShopSecurityException("Error in generate token: " + pet.getMessage());
        }
    }

    public String validateToken(String token){

        Algorithm algorithm = Algorithm.HMAC256(secret); //use the same encryption

        try{
            return JWT
                    .require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        }catch (JWTVerificationException pet){

            return "";

        }

    }

    private Instant petTimesExpires() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }


}
