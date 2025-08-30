package alik.receiptmaker.auth;

import alik.receiptmaker.model.EmailRequest;
import alik.receiptmaker.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping
    public void register(@RequestBody @Valid RegisterRequest request) {
        registerService.register(request);
    }

    @PostMapping("/send-code")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody @Valid EmailRequest request) {
        emailVerificationService.sendVerificationCode(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyCode(@RequestBody @Valid EmailRequest request, @RequestParam String code) {
        emailVerificationService.verifyCode(request.getEmail(), code);
        return ResponseEntity.ok().build();

    }

}
