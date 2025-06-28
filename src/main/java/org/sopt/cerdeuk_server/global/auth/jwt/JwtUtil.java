package org.sopt.cerdeuk_server.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.sopt.cerdeuk_server.global.error.code.ErrorCode;
import org.sopt.cerdeuk_server.global.error.exception.BadRequestException;
import org.sopt.cerdeuk_server.global.error.exception.InvalidValueException;
import org.sopt.cerdeuk_server.global.error.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;

    private final long accessTokenExpirationTime;

    private final long refreshTokenExpirationTime;

    private static final String USER_ID_PREFIX = "userId";
    private static final String BEARER = "Bearer ";

    public JwtUtil(
        @Value("${jwt.secret}") String jwtSecret,
        @Value("${jwt.access-token-expiration-time}") long accessTokenExpirationTime,
        @Value("${jwt.refresh-token-expiration-time}") long refreshTokenExpirationTime
    ){
        this.secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public Claims getClaims(String token){
        try{
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (JwtException e){
            log.error("Jwt Token 파싱 실패: {}", e.getMessage());
            throw new UnauthorizedException();
        }
    }

    public String extractToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith(BEARER)){
            log.info("jwt 토큰 누락");
            throw new UnauthorizedException();
        }

        return authorization.substring(BEARER.length());
    }

    public Long getUserId(String token){
        Claims claims = getClaims(token);
        try{
            return claims.get("userId", Long.class);
        }catch (IllegalArgumentException e){
            log.error("토큰 유저 정보 누락: {}", e.getMessage());
            throw new UnauthorizedException();
        }
    }

    public boolean isTokenExpired(String token){
        Claims claims = getClaims(token);
        try{
            return claims.getExpiration().before(new Date());
        }catch (IllegalArgumentException e){
            log.error("토큰 expiration 누락: {}", e.getMessage());
            throw new UnauthorizedException();
        }
    }

    public String createAccessToken(Long userId){
        return BEARER + createToken(userId, accessTokenExpirationTime);
    }

    public String createRefreshToken(Long userId){
        return BEARER + createToken(userId, refreshTokenExpirationTime);
    }

    public String createToken(Long userId, long expirationTime){
        return Jwts.builder()
                .claim(USER_ID_PREFIX, userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

}
