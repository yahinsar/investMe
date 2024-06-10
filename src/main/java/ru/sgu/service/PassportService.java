package ru.sgu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgu.model.Passport;
import ru.sgu.repository.PassportRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PassportService {

    private static final Logger logger = LoggerFactory.getLogger(PassportService.class);

    private final PassportRepository passportRepository;

    @Autowired
    public PassportService(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    public void savePassport(Passport passport) {
        logger.debug("Сохранены паспортные данные для пользователя: {}", passport.getUser().getUsername());
        passportRepository.save(passport);
    }

    public Passport save(Passport passport) {
        return passportRepository.save(passport);
    }

    public Optional<Passport> findById(Long id) {
        logger.debug("В результате поиска по id паспорта {} найден и возвращен паспорт.", id);
        return passportRepository.findById(id);
    }

    public void deleteById(Long id) {
        logger.debug("В результате поиска по id паспорта {} удален паспорт.", id);
        passportRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        logger.debug("В результате поиска по id {} найден паспорт.", id);
        return passportRepository.existsById(id);
    }

    public Passport findByUserId(Long userId) {
        logger.debug("В результате поиска по user_id {} найден и возвращен паспорт.", userId);
        return passportRepository.findByUserId(userId);
    }

    public boolean isUser18yo(Long userId) {
        logger.info("Проверка пользователя с user_id {} на совершеннолетие", userId);
        Passport passport = passportRepository.findByUserId(userId);
        if (passport == null) {
            throw new RuntimeException("Паспорт пользователя с user id = \"" + userId + "\" не найден.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(passport.getDateOfBirth(), formatter);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        boolean is18yo = period.getYears() >= 18;
        logger.info("Пользователь с user_id {} {} 18-ти лет", userId, is18yo ? "достиг" : "не достиг");
        return is18yo;
    }
}