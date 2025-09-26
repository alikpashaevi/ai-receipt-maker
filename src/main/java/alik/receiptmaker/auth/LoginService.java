package alik.receiptmaker.auth;

import alik.receiptmaker.error.InvalidLoginException;
import alik.receiptmaker.user.UserService;
import alik.receiptmaker.user.persistence.AppUser;
import alik.receiptmaker.user.persistence.Role;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        if(userService.getUser(request.getUsername()) == null) {
            throw new InvalidLoginException("Invalid username or password");
        } else {
            AppUser user = userService.getUser(request.getUsername());
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return generateLoginResponse(user);
            }
        };
        throw new InvalidLoginException("Invalid username or password");
    }

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

            return new LoginResponse(signedJWT.serialize());
        } catch (Exception e) {
            throw new InvalidLoginException("Error generating JWT token");
        }
    }

}
