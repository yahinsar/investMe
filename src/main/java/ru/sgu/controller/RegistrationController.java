package ru.sgu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sgu.service.UserService;
import ru.sgu.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    //Отслеживает нажатие на ссылку из письма и вызывает активацию аккаунта
    @GetMapping("/api/v1/registration/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        try {
            boolean isActivated = userService.activateUser(token);
            if (isActivated) {
                return ResponseEntity.ok("Аккаунт активирован!");
            } else {
                return ResponseEntity.ok("Активация не удалась.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }

    @PostMapping("/api/v1/registration")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
            return ResponseEntity.ok("Пользователь зарегистрирован, необходимо активировать учётную запись, нажав на ссылку в письме");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }
}