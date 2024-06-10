package ru.sgu.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sgu.model.User;
import ru.sgu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationController registrationController;

    @Test
    void activateAccount_shouldActivateAccount() {
        when(userService.activateUser("token")).thenReturn(true);

        ResponseEntity<String> responseEntity = registrationController.activateAccount("token");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Аккаунт активирован!");
    }

    @Test
    void activateAccount_shouldReturnFailureMessage() {
        when(userService.activateUser("token")).thenReturn(false);

        ResponseEntity<String> responseEntity = registrationController.activateAccount("token");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Активация не удалась.");
    }

    @Test
    void registerUser_shouldRegisterUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        doNothing().when(userService).registerUser("testUser", "test@example.com", "password");

        ResponseEntity<String> responseEntity = registrationController.registerUser(user);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Пользователь зарегистрирован, необходимо активировать учётную запись, нажав на ссылку в письме");
    }
}