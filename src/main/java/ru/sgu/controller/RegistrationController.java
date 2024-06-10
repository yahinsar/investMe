package ru.sgu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sgu.service.UserService;
import ru.sgu.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    //Отслеживает нажатие на ссылку из письма и вызывает активацию аккаунта
    @GetMapping("/api/v1/registration/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        logger.info("Получен запрос на активацию аккаунта с токеном: {}", token);
        try {
            boolean isActivated = userService.activateUser(token);
            if (isActivated) {
                logger.info("Аккаунт успешно активирован.");
                return ResponseEntity.ok("Аккаунт активирован!");
            } else {
                logger.warn("Не удалось активировать аккаунт.");
                return ResponseEntity.ok("Активация не удалась.");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка активации аккаунта: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Внутренняя ошибка сервера при активации аккаунта.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }

    @PostMapping("/api/v1/registration")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        logger.info("Получен запрос на регистрацию пользователя: {}", user.getUsername());
        try {
            userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
            logger.info("Пользователь успешно зарегистрирован: {}", user.getUsername());
            return ResponseEntity.ok("Пользователь зарегистрирован, необходимо активировать учётную запись, нажав на ссылку в письме");
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка регистрации пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Внутренняя ошибка сервера при регистрации пользователя.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }
}