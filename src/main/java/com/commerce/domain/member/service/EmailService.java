package com.commerce.domain.member.service;

import com.commerce.web.member.dto.EmailSendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Transactional
    public void sendMail(EmailSendDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        StringBuilder sb = new StringBuilder();
        String email = "email=" + dto.getTo();
        String token = "&token=" + UUID.randomUUID().toString().substring(0, 10);
        String valid = "&" + dto.getText();
        String url = "http://localhost:8080/api/v1/member/confirm-email?";
        String result = sb.append(url).append(email).append(token).append(valid).toString();

        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        message.setText(result);
        message.setFrom("devpet0327@gmail.com"); // 보내는 사람 이메일 주소

        mailSender.send(message);
    }

}
