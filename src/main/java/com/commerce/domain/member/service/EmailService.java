package com.commerce.domain.member.service;

import com.commerce.domain.member.entity.Verification;
import com.commerce.domain.member.entity.cache.CacheVerification;
import com.commerce.domain.member.repository.VerificationRepository;
import com.commerce.web.member.dto.request.EmailSendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final VerificationRepository verificationRepository;
    // Redis
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void sendMail(EmailSendDto dto) {
        String token = generateToken();
        String confirmationUrl = buildConfirmationUrl(dto.getTo(), token, dto.getText());
        CacheVerification cacheVerification
                = CacheVerification.cacheToken(token, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                false, false, false);

        // 토큰 Redis 저장
        redisTemplate.opsForValue().set(dto.getTo(), cacheVerification);

        // 토큰 DB 저장
        saveVerificationToken(dto.getTo(), token);

        sendEmail(dto, confirmationUrl);
    }

    // 토큰 저장
    private void saveVerificationToken(String email, String token) {
        Verification verification = Verification.createVerification(
                email, token, LocalDateTime.now(), false, false, false);
        verificationRepository.save(verification);
    }

    // 토큰 만료.
    public void updateExpiredToken(Verification verification) {
        verification.emailTokenKill(true, true, true);
        verificationRepository.save(verification);
    }

    // 이메일 인증 토큰 발급
    private String generateToken() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    // 메일확인 url 만들기.
    private String buildConfirmationUrl(String email, String token, String text) {
        return "http://localhost:8080/api/member/confirm-email?" +
                "email=" + email +
                "&token=" + token +
                "&" + text;
    }

    // 메일발송 메서드
    private void sendEmail(EmailSendDto dto, String confirmationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        message.setText(confirmationUrl);
        message.setFrom("devpet0327@gmail.com");

        mailSender.send(message);
    }

    // 아카이브
    @Deprecated
    @Transactional
    public void before_sendMail(EmailSendDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        StringBuilder sb = new StringBuilder();
        String email = "email=" + dto.getTo();
        String token = UUID.randomUUID().toString().substring(0, 10);
        String valid = "&" + dto.getText();
        String url = "http://localhost:8080/api/member/confirm-email?";
        String result = sb.append(url).append(email)
                .append("&token=").append(token)
                .append(valid)
                .toString();

        Verification verification = Verification
                .createVerification(dto.getTo(), token, LocalDateTime.now(),
                        false, false, false);
//        verificationRepository.save(verification);

        // 메일 발송
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        message.setText(result);
        message.setFrom("devpet0327@gmail.com"); // 보내는 사람 이메일 주소

//        mailSender.send(message);
    }
}
