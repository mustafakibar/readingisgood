package kibar.readingisgood.security;

import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

//@Service
public class JwtSigner {

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS512;
    private final KeyPair keyPair = Keys.keyPairFor(signatureAlgorithm);

    public String create(String mail) {
        Instant now = Instant.now();

        return Jwts.builder()
                .signWith(keyPair.getPrivate(), signatureAlgorithm)
                .setSubject(mail)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(Duration.ofHours(1))))
                .compact();
    }

    public Jws<Claims> validate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token);
    }

}
