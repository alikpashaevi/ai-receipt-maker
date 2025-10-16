package alik.receiptmaker.service;

import alik.receiptmaker.model.EmailRequest;
import alik.receiptmaker.persistence.EmailVerification;
import alik.receiptmaker.persistence.EmailVerificationRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepo emailVerificationRepo;
    private final GmailService gmailService;

    @Transactional
    public void sendVerificationCode(EmailRequest email) {
        String code = String.valueOf((int) (Math.random() * 900000) + 100000);

        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        emailVerificationRepo.deleteByEmail(email.getEmail());

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(email.getEmail());
        emailVerification.setCode(code);
        emailVerification.setExpirationTime(expirationTime);
        emailVerificationRepo.save(emailVerification);

        gmailService.sendEmail(email.getEmail(), "RecipeAI Verification Code", "Thank you for signing up for RecipeAI! Please use this code to verify your email address. This code will expire in 10 minutes. \n\n CODE: " + code);

    }

    @Transactional
    public void verifyCode(String email, String code) {
        Optional<EmailVerification> verificationOpt = emailVerificationRepo.findByEmail(email);

        if (verificationOpt.isEmpty()) return;

        EmailVerification verification = verificationOpt.get();

        if (verification.getExpirationTime().isBefore(LocalDateTime.now())) {
            emailVerificationRepo.deleteByEmail(email);
            return;
        }

        boolean isValid = verification.getCode().equals(code);

        if (isValid) {
            emailVerificationRepo.deleteByEmail(email);
        }

    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredCodes() {
        emailVerificationRepo.deleteByExpirationTimeBefore(LocalDateTime.now());
    }

}