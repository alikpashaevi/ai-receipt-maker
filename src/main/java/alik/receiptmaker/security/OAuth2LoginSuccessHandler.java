package alik.receiptmaker.security;

import alik.receiptmaker.auth.JwtService;
import alik.receiptmaker.auth.LoginService;
import alik.receiptmaker.user.CustomOAuth2User;
import alik.receiptmaker.user.persistence.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        AppUser user = oauth2User.getAppUser();

        var loginResponse = jwtService.generateLoginResponse(user);
        String token = loginResponse.getAccessToken();

//        response.setContentType("application/json");
//        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
        boolean firstLogin = user.getFirstLogin();
        response.sendRedirect("http://localhost:3000/oauth2/success?token=" + token + "&firstLogin=" + firstLogin);
    }
}
