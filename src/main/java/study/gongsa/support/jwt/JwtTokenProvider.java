package study.gongsa.support.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secretKey}")
    private String secretKey;
    private String tokenPrefix = "Bearer ";

    public String makeAccessToken(int userUID) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofHours(1).toMillis()))
                .claim("userUID", userUID)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String makeRefreshToken(int userUID) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(14).toMillis()))
                .claim("userUID", userUID)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims parsingToken(String authorizationHeader) {
        validationAuthorizationHeader(authorizationHeader);
        String token = extractToken(authorizationHeader);
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private void validationAuthorizationHeader(String header) {
        if (header == null || !header.startsWith(tokenPrefix)) {
            throw new IllegalArgumentException();
        }
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(tokenPrefix.length());
    }

}