package com.project.shopapp.components;

import com.project.shopapp.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
   @Value("${jwt.expiration}")
   private int expiration; //tgian ket thuc token

   @Value("${jwt.secretKey}")
   private String secretKey;

   public String generateToken(User user) {
      //properties => claims
      Map<String, Object> claims = new HashMap<>();
      claims.put("phoneNumber", user.getPhoneNumber());
      try {
         String token = Jwts.builder()
                 .setClaims(claims)
                 .setSubject(user.getPhoneNumber())
                 .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                 .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                 .compact();
         return token;
      } catch (Exception e) {
         // có thể dùng cách xem khác: Logger thay System.out.println
         System.err.println("Khong the tao JWT"+e.getMessage());
         return null;
      }
   }

   private Key getSignInKey() {
      byte[] bytes = Decoders.BASE64.decode(secretKey);
      return Keys.hmacShaKeyFor(bytes);

   }
   private Claims extractAllClaims(String token){
      return Jwts.parserBuilder()
              .setSigningKey(getSignInKey())
              .build()
              .parseClaimsJwt(token)
              .getBody();
   }
private <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
     final Claims claims = this.extractAllClaims(token);
     return claimsResolver.apply(claims);
     }
//check expiration
   public boolean isTokenExpired(String token){
      Date expirationDate = this.extractClaims(token, Claims::getExpiration);
      return expirationDate.before(new Date());
   }


}

