package ru.sgu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.sgu.model.AuthenticationRequest;
import ru.sgu.model.AuthenticationResponse;
import ru.sgu.util.JwtUtil;
import ru.sgu.service.MyUserDetailsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1")

public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("Получен запрос на аутентификацию пользователя: {}", authenticationRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);
            logger.info("Пользователь {} успешно аутентифицирован, сгенерирован JWT.", userDetails.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse(jwt));

        } catch (AuthenticationException e) {
            logger.error("Ошибка аутентификации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильно указан username или password");
        }
    }

    @GetMapping("/protected")
    public ResponseEntity<String> getProtectedResource() {
        logger.info("Запрос к защищенному ресурсу.");
        return ResponseEntity.ok("Проверка get запроса по jwt токену (http://localhost:8080/api/v1/protected) прошла, доступ есть.");
    }
}