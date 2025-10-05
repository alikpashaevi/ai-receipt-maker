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

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        if(userService.getUserForLogin(request.getUsername()) == null) {
            throw new InvalidLoginException("Invalid username or password");
        } else {
            AppUser user = userService.getUser(request.getUsername());
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return jwtService.generateLoginResponse(user);
            }
        };
        throw new InvalidLoginException("Invalid username or password");
    }



}
