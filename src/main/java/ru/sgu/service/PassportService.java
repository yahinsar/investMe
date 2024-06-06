package ru.sgu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgu.model.Passport;
import ru.sgu.repository.PassportRepository;

import java.util.Optional;

@Service
public class PassportService {

    @Autowired
    private PassportRepository passportRepository;

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
}
