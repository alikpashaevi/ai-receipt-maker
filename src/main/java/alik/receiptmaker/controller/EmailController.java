package alik.receiptmaker.controller;

import alik.receiptmaker.service.GmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final GmailService gmailService;

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestParam String email, @RequestParam String subject, @RequestParam String body) {
        gmailService.sendEmail(email, subject, body);
        return ResponseEntity.ok().build();
    }

}
