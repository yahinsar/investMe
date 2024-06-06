package ru.sgu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.sgu.model.Passport;
import ru.sgu.model.User;
import ru.sgu.service.PassportService;
import ru.sgu.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/passport")
public class PassportController {

    private final PassportService passportService;
    private final UserService userService;

    @Autowired
    public PassportController(PassportService passportService, UserService userService) {
        this.passportService = passportService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Passport> createPassport(@RequestBody Passport passport) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            passport.setUser(user.get());
            Passport savedPassport = passportService.save(passport);
            return ResponseEntity.ok(savedPassport);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passport> getPassport(@PathVariable Long id) {
        Optional<Passport> passport = passportService.findById(id);
        if (passport.isPresent()) {
            return ResponseEntity.ok(passport.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passport> updatePassport(@PathVariable Long id, @RequestBody Passport passportDetails) {
        Optional<Passport> passport = passportService.findById(id);
        if (passport.isPresent()) {
            Passport passportToUpdate = passport.get();
            passportToUpdate.setLastName(passportDetails.getLastName());
            passportToUpdate.setFirstName(passportDetails.getFirstName());
            passportToUpdate.setMiddleName(passportDetails.getMiddleName());
            passportToUpdate.setGender(passportDetails.getGender());
            passportToUpdate.setDateOfBirth(passportDetails.getDateOfBirth());
            passportToUpdate.setPlaceOfBirth(passportDetails.getPlaceOfBirth());
            passportToUpdate.setPassportSeries(passportDetails.getPassportSeries());
            passportToUpdate.setPassportNumber(passportDetails.getPassportNumber());
            passportToUpdate.setIssueDate(passportDetails.getIssueDate());
            passportToUpdate.setIssuedBy(passportDetails.getIssuedBy());
            passportToUpdate.setDepartmentCode(passportDetails.getDepartmentCode());
            passportToUpdate.setRegistrationPlace(passportDetails.getRegistrationPlace());
            passportToUpdate.setResidencePlace(passportDetails.getResidencePlace());
            Passport updatedPassport = passportService.save(passportToUpdate);
            return ResponseEntity.ok(updatedPassport);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassport(@PathVariable Long id) {
        if (passportService.existsById(id)) {
            passportService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Passport> getPassportForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            Passport passport = passportService.findByUserId(user.get().getId());
            if (passport != null) {
                return ResponseEntity.ok(passport);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check-age")
    public ResponseEntity<String> checkUserAge() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean is18yo = passportService.isUser18yo(user.getId());
        if (is18yo) {
            return ResponseEntity.ok("Добро пожаловать в ИНВЕСТИЦИИ");
        } else {
            return ResponseEntity.ok("Тебе доступ запрещен. Пошел вон отсюда.");
        }
    }
}
