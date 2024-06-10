package ru.sgu.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sgu.model.Passport;
import ru.sgu.repository.PassportRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassportServiceTest {

    @Mock
    private PassportRepository passportRepository;

    @InjectMocks
    private PassportService passportService;

    @Test
    void savePassport_shouldSavePassport() {
        Passport passport = new Passport();
        passport.setFirstName("Test");

        when(passportRepository.save(passport)).thenReturn(passport);

        passportService.savePassport(passport);

        verify(passportRepository, times(1)).save(passport);
    }

    @Test
    void findById_shouldReturnPassport() {
        Passport passport = new Passport();
        passport.setFirstName("Test");

        when(passportRepository.findById(1L)).thenReturn(Optional.of(passport));

        Optional<Passport> foundPassport = passportService.findById(1L);

        assertThat(foundPassport).isPresent();
        assertThat(foundPassport.get().getFirstName()).isEqualTo("Test");
    }

    @Test
    void isUser18yo_shouldReturnTrueIfUserIs18() {
        Passport passport = new Passport();
        passport.setDateOfBirth("2000-01-01");
        when(passportRepository.findByUserId(1L)).thenReturn(passport);

        boolean is18yo = passportService.isUser18yo(1L);

        assertThat(is18yo).isTrue();
    }

    @Test
    void isUser18yo_shouldReturnFalseIfUserIsNot18() {
        Passport passport = new Passport();
        passport.setDateOfBirth("2010-01-01");
        when(passportRepository.findByUserId(1L)).thenReturn(passport);

        boolean is18yo = passportService.isUser18yo(1L);

        assertThat(is18yo).isFalse();
    }
}