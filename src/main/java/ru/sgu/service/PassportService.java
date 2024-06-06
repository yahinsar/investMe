package ru.sgu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgu.model.Passport;
import ru.sgu.repository.PassportRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

@Service
public class PassportService {

    private final PassportRepository passportRepository;

    @Autowired
    public PassportService(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    public Passport save(Passport passport) {
        return passportRepository.save(passport);
    }

    public Optional<Passport> findById(Long id) {
        return passportRepository.findById(id);
    }

    public void deleteById(Long id) {
        passportRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return passportRepository.existsById(id);
    }

    public Passport findByUserId(Long userId) {
        return passportRepository.findByUserId(userId);
    }

    public boolean isUser18yo(Long userId) {
        Passport passport = passportRepository.findByUserId(userId);
        if (passport == null) {
            throw new RuntimeException("Паспорт пользователя с user id = \"" + userId + "\" не найден.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(passport.getDateOfBirth(), formatter);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears() >= 18;
    }
}
