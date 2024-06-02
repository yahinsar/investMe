package ru.sgu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(String recipientEmail, String activationToken) {

        //Тема письма
        String subject = "Активация аккаунта";

        //Содержимое письма
        String content = "Добро пожаловать,<br>"
                + "Для активации аккаунта необходимо нажать на ссылку ниже:<br>"
                + "<h3><a href=\"http://localhost:8080/api/v1/registration/activate?token=" + activationToken + "\">АКТИВИРОВАТЬ</a></h3>"
                + "Благодарим за выбор нашей компании,<br>"
                + "команда InvestMe";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            //Куда, откуда, тема, содержимое
            helper.setTo(recipientEmail);
            helper.setFrom("investMeJavaApp@yandex.ru");
            helper.setSubject(subject);
            helper.setText(content, true);

            //Отправка
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new IllegalArgumentException("Ошибка при отправке письма с активацией", e);
        }
    }
}