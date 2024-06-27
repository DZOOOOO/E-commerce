package com.commerce.domain.member.service;

import com.commerce.domain.member.entity.Verification;
import com.commerce.domain.member.repository.VerificationRepository;
import com.commerce.web.member.dto.request.EmailSendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final VerificationRepository verificationRepository;

    @Transactional
    public void sendMail(EmailSendDto dto) {
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
        verificationRepository.save(verification);

        // 메일 발송
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        message.setText(result);
        message.setFrom("devpet0327@gmail.com"); // 보내는 사람 이메일 주소

        mailSender.send(message);
    }

    // 토큰 만료.
    public void updateExpiredToken(Verification verification) {
        verification.emailTokenKill(true, true, true);
        verificationRepository.save(verification);
    }

}
