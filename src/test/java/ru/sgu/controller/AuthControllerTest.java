package ru.sgu.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.sgu.model.AuthenticationRequest;
import ru.sgu.model.AuthenticationResponse;
import ru.sgu.service.MyUserDetailsService;
import ru.sgu.util.JwtUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    void createAuthenticationToken_shouldReturnJwtToken() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUser", "password");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("jwtToken");

        ResponseEntity<?> responseEntity = authController.createAuthenticationToken(authenticationRequest);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(((AuthenticationResponse) responseEntity.getBody()).getJwtToken()).isEqualTo("jwtToken");
    }

    @Test
    void createAuthenticationToken_shouldReturnUnauthorized() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("testUser", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException(""));

        ResponseEntity<?> responseEntity = authController.createAuthenticationToken(authenticationRequest);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
        assertThat(responseEntity.getBody()).isEqualTo("Неправильно указан username или password");
    }
}
