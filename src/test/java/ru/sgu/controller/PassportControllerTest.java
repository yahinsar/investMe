package ru.sgu.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.sgu.model.Passport;
import ru.sgu.model.User;
import ru.sgu.service.PassportService;
import ru.sgu.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassportControllerTest {

    @Mock
    private PassportService passportService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PassportController passportController;

    @Test
    void createPassport_shouldReturnBadRequestWhenInvalid() {
        Passport passport = new Passport();
        passport.setFirstName("");

        ResponseEntity<Passport> responseEntity = passportController.createPassport(passport);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createPassport_shouldReturnOkWhenValid() {
        Passport passport = new Passport();
        passport.setFirstName("Test");

        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(auth.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(new User()));
        when(passportService.save(any(Passport.class))).thenReturn(passport);

        ResponseEntity<Passport> responseEntity = passportController.createPassport(passport);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getFirstName()).isEqualTo("Test");
    }

    @Test
    void getPassportForCurrentUser_shouldReturnPassport() {
        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(auth.getName()).thenReturn("testUser");
        User user = new User();
        user.setId(1L);
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(user));

        Passport passport = new Passport();
        passport.setFirstName("Test");
        when(passportService.findByUserId(1L)).thenReturn(passport);

        ResponseEntity<Passport> responseEntity = passportController.getPassportForCurrentUser();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getFirstName()).isEqualTo("Test");
    }

    @Test
    void checkUserAge_shouldReturnWelcomeMessageWhenUserIs18() {
        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(auth.getName()).thenReturn("testUser");
        User user = new User();
        user.setId(1L);
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passportService.isUser18yo(1L)).thenReturn(true);

        ResponseEntity<String> responseEntity = passportController.checkUserAge();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Добро пожаловать в ИНВЕСТИЦИИ");
    }

    @Test
    void checkUserAge_shouldReturnAccessDeniedMessageWhenUserIsNot18() {
        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(auth.getName()).thenReturn("testUser");
        User user = new User();
        user.setId(1L);
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passportService.isUser18yo(1L)).thenReturn(false);

        ResponseEntity<String> responseEntity = passportController.checkUserAge();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Тебе доступ запрещен. Пошел вон отсюда.");
    }
}