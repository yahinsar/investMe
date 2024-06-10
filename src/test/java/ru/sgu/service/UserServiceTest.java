package ru.sgu.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sgu.model.User;
import ru.sgu.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldRegisterUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.registerUser("testUser", "test@example.com", "password");

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendActivationEmail(eq("test@example.com"), anyString());
    }

    @Test
    void activateUser_shouldActivateUser() {
        User user = new User();
        user.setEnabled(false);
        user.setActivationToken("token");

        when(userRepository.findByActivationToken("token")).thenReturn(Optional.of(user));

        boolean isActivated = userService.activateUser("token");

        assertThat(isActivated).isTrue();
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getActivationToken()).isNull();
        verify(userRepository, times(1)).save(user);
    }
}