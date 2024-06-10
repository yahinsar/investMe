package ru.sgu.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.security.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final Key SECRET_KEY;

    public JwtUtil() {
        this.SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        logger.info("Инициализация секретного ключа для JWT.");
    }

    // извлечение данных из токена типа T (claimsResolver - нужная функция из Claims)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // извлечение имени пользователя из токена
    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        return username;
    }

    // извлечение даты истечения токена
    public Date extractExpiration(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration;
    }

    // извлечение всех данных из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // проверка, просрочен ли токен
    private Boolean isTokenExpired(String token) {
        boolean isExpired = extractExpiration(token).before(new Date());
        return isExpired;
    }

    // генерация токена
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, userDetails.getUsername());
        logger.info("Генерация токена для пользователя: {}", userDetails.getUsername());
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
        logger.info("Создание токена для субъекта: {}", subject);
        return token;
    }

    // проверка токена на валидность
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        return isValid;
    }

}