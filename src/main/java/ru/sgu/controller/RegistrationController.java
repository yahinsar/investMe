package ru.sgu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sgu.service.UserService;

@RestController
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    //Отслеживает нажатие на ссылку из письма и вызывает активацию аккаунта
    @GetMapping("/api/v1/registration/activate")
    public String activateAccount(@RequestParam("token") String token) {
        boolean isActivated = userService.activateUser(token);
        if (isActivated) {
            return "Аккаунт активирован!";
        } else {
            return "Активация не удалась.";
        }
    }
}