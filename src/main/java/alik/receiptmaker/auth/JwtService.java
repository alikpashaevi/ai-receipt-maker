package alik.receiptmaker.auth;

import alik.receiptmaker.error.InvalidLoginException;
import alik.receiptmaker.user.persistence.AppUser;
import alik.receiptmaker.user.persistence.Role;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    public LoginResponse generateLoginResponse(AppUser user) {
        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .claim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                    .issuer("receiptmaker.ge")
                    .expirationTime(new Date(new Date().getTime() + 1000 * 60 * 60 * 24))
                    .build();

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            SignedJWT signedJWT = new SignedJWT(header, claims);
            signedJWT.sign(new MACSigner(secretKey.getBytes()));

            return new LoginResponse(signedJWT.serialize(), user.getFirstLogin());
        } catch (Exception e) {
            throw new InvalidLoginException("Error generating JWT token");
        }
    }
}
