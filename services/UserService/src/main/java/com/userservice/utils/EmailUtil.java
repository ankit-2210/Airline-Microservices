package com.userservice.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender mailSender;
    public void sendResetMail(String toEmail, String url) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("aki.code22@gmail.com", "Airline Management");
        helper.setTo(toEmail);

        String content = """
                <p>Hello,</p>
                <p>You requested to reset your password.</p>
                <p>Click below:</p>
                <p><a href="%s">Reset Password</a></p>
                <p>This link expires in 15 minutes.</p>
                """.formatted(url);

        helper.setSubject("Password Reset");
        helper.setText(content, true);

        mailSender.send(message);

    }

}
