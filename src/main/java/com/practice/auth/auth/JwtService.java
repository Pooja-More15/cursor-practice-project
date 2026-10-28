package com.practice.auth.auth;

import com.practice.auth.config.JwtProperties;
import com.practice.auth.user.AppUser;
import java.time.Instant;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final JwtEncoder jwtEncoder;
  private final JwtProperties properties;

  public JwtService(JwtEncoder jwtEncoder, JwtProperties properties) {
    this.jwtEncoder = jwtEncoder;
    this.properties = properties;
  }

  public String createToken(AppUser user) {
    Instant now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer(properties.issuer())
            .issuedAt(now)
            .expiresAt(now.plus(properties.expiration()))
            .subject(user.getUsername())
            .claim("role", user.getRole())
            .build();
    return jwtEncoder
        .encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
        .getTokenValue();
  }
}
