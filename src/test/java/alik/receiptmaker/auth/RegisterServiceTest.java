package alik.receiptmaker.auth;
// src/test/java/alik/receiptmaker/auth/RegisterServiceTest.java
import alik.receiptmaker.error.UserExistsException;
import alik.receiptmaker.user.persistence.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import alik.receiptmaker.user.RoleService;
import alik.receiptmaker.user.persistence.AppUserRepo;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private AppUserRepo appUserRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleService roleService;

    @InjectMocks
    private RegisterService registerService;

    @Test
    void register_successful() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setEmail("test@example.com");

        when(appUserRepo.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleService.getRole(2L)).thenReturn(Collections.emptySet());

        registerService.register(request);

        verify(appUserRepo, times(1)).save(any(AppUser.class));
    }

    @Test
    void register_usernameExists_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setPassword("password");
        request.setEmail("existing@example.com");

        when(appUserRepo.existsByUsername("existinguser")).thenReturn(true);

        assertThrows(UserExistsException.class, () -> registerService.register(request));
        verify(appUserRepo, never()).save(any(AppUser.class));
    }
}