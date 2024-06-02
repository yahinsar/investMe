package ru.sgu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sgu.model.User;
import ru.sgu.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String email, String password) {
        //Заполнение полей пользователя
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setEnabled(false);

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        //Генерация токена для активации
        String activationToken = java.util.UUID.randomUUID().toString();
        user.setActivationToken(activationToken);

        //Сохранение пользователя в БД (НУЖНО ДОБАВИТЬ ОБРАБОТКУ ТОГО, ЧТО ПОЛЬЗОВАТЕЛЬ УЖЕ ЕСТЬ)
        userRepository.save(user);

        //Отправка письма на почту
        emailService.sendActivationEmail(email, activationToken);
    }

    public boolean activateUser(String token) {
        Optional<User> optionalUser = userRepository.findByActivationToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            //Меняет статус аккаунта на активированный
            user.setEnabled(true);

            //Сбрасывает токен активации
            user.setActivationToken(null);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}