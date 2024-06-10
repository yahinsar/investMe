package ru.sgu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sgu.model.User;
import ru.sgu.repository.UserRepository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
        logger.info("Попытка регистрации пользователя с username: {} и почтой: {}", username, email);
        if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            logger.warn("Пользователь с username: {} или email: {} уже существует.", username, email);
            throw new IllegalArgumentException("Пользователь с таким username или email уже существует.");
        }

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

        //Сохранение пользователя в БД
        userRepository.save(user);
        logger.info("Успешно зарегистрирован пользователь с username: {}", username);

        //Отправка письма на почту
        emailService.sendActivationEmail(email, activationToken);

    }

    public boolean activateUser(String token) {
        logger.info("Попытка активации пользователя с токеном: {}", token);
        Optional<User> optionalUser = userRepository.findByActivationToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            //Меняет статус аккаунта на активированный
            user.setEnabled(true);

            //Сбрасывает токен активации
            user.setActivationToken(null);
            userRepository.save(user);
            logger.info("Успешно активирован пользователь с username: {}", user.getUsername());
            return true;
        } else {
            return false;
        }
    }

    public Optional<User> findByUsername(String username) {
        logger.info("Поиск пользователя по username: {}", username);
        return userRepository.findByUsername(username);
    }

    public boolean isEnabled(String username) {
        logger.info("Проверка пользователя с username {} на активированный аккаунт", username);
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(User::isEnabled).orElse(false);
    }
}