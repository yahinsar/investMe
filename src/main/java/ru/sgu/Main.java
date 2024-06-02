package ru.sgu;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.sgu.model.User;
import ru.sgu.service.EmailService;
import ru.sgu.service.UserService;

import java.util.Optional;

@SpringBootApplication
//Добавление пользователя в БД и его активация
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner demo(JdbcTemplate jdbcTemplate, UserService userService, EmailService emailService) {
        return (args) -> {

            String username = "testuser";
            String email = "arbuzov.matv.y@yandex.ru";
            String password = "password";

            //Добавление пользователя в БД и отправка письма на почту
            userService.registerUser(username, email, password);
            System.out.println("Пользователь зарегистрирован, необходимо активировать учётную запись," +
                    " нажав на ссылку в письме");

        };
    }
}