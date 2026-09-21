package com.userservice.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String senderName;

    public void sendResetMail(String toEmail, String resetUrl){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, senderName);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <body style="font-family: Arial, sans-serif;">

                        <h2>Password Reset</h2>

                        <p>Hello,</p>

                        <p>
                            We received a request to reset your password.
                        </p>

                        <p>
                            Click the button below to reset your password:
                        </p>

                        <p>
                            <a href="%s"
                               style="
                               display:inline-block;
                               padding:12px 20px;
                               background:#2563eb;
                               color:white;
                               text-decoration:none;
                               border-radius:6px;">
                                Reset Password
                            </a>
                        </p>

                        <p>
                            This link will expire in
                            <b>15 minutes</b>.
                        </p>

                        <p>
                            If you did not request this password reset,
                            you can safely ignore this email.
                        </p>

                        <br>

                        <p>
                            Regards,<br>
                            Airline Management Team
                        </p>

                    </body>
                    </html>
                    """.formatted(resetUrl);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        }
        catch (MessagingException | UnsupportedEncodingException e){
            throw new RuntimeException("Failed to send password reset email", e);
        }

    }

}
