package alik.receiptmaker.controller;

import alik.receiptmaker.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestParam String email, @RequestParam String subject, @RequestParam String body) {
        emailService.sendEmail(email, subject, body);
        return ResponseEntity.ok().build();
    }

}
