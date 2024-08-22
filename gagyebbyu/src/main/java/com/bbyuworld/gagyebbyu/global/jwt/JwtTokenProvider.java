package com.bbyuworld.gagyebbyu.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret_key}")
    private String SECRET_KEY;
    @Value("${jwt.access_token_expiration_time}")
    private Long ACCESS_TOKEN_EXPIRATION_TIME;
    @Value("${jwt.refresh_token_expiration_time}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    public JwtToken createToken(Long userId){
        String accessToken = createAccessToken(userId);
        String refreshToken = createRefreshToken(userId);
        return JwtToken.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public boolean validateToken(String accessToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken);
            return true;
        } catch (SignatureException ex) {
            ex.printStackTrace();
        } catch (MalformedJwtException ex) {
            ex.printStackTrace();
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
        } catch (UnsupportedJwtException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public Long getUserId(String accessToken){
        Claims claims = null;
        try{
            claims = parseClaims(accessToken);
            return claims.get("userId", Long.class);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(accessToken.replace("Bearer ", ""))
                .getBody();
    }


    private String createAccessToken(Long userId){
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ");
        sb.append(
                Jwts.builder()
                        .setSubject(userId.toString())
                        .claim("userId", userId)
                        .claim("authorities", "USER")
                        .setExpiration(
                                new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME)
                        )
                        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                        .compact()
        );
        return sb.toString();
    }

    private String createRefreshToken(Long userId){
        return UUID.randomUUID().toString()+"-"+System.currentTimeMillis();
    }

}
