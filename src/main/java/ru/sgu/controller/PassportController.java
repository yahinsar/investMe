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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
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
    public ResponseEntity<String> createPassport(@RequestBody Passport passport) {
        String validationMessage = validatePassportData(passport);
        if (!validationMessage.isEmpty()) {
            return ResponseEntity.badRequest().body(validationMessage);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        passport.setUser(user);
        passportService.savePassport(passport);

        String welcomeMessage = generateWelcomeMessage(passport);
        return ResponseEntity.ok(welcomeMessage);
    }

    private String validatePassportData(Passport passport) {
        if (passport.getLastName() == null || passport.getLastName().isEmpty())
            return "Фамилия не может быть пустой.";
        if (passport.getFirstName() == null || passport.getFirstName().isEmpty())
            return "Имя не может быть пустым.";
        if (passport.getMiddleName() == null || passport.getMiddleName().isEmpty())
            return "Отчество не может быть пустым.";
        if (passport.getGender() == null || (!passport.getGender().equals("Мужской") && !passport.getGender().equals("Женский")))
            return "Пол не может быть пустым (введите \"Мужской\" или \"Женский\").";
        if (!isValidDate(passport.getDateOfBirth()))
            return "Дата рождения должна быть в формате YYYY-MM-DD.";
        if (passport.getPlaceOfBirth() == null || passport.getPlaceOfBirth().isEmpty())
            return "Место рождения не может быть пустым.";
        if (passport.getPassportSeries() == null || !Pattern.matches("\\d{4}", passport.getPassportSeries()))
            return "Серия паспорта должна состоять из 4 цифр.";
        if (passport.getPassportNumber() == null || !Pattern.matches("\\d{6}", passport.getPassportNumber()))
            return "Номер паспорта должен состоять из 6 цифр.";
        if (!isValidDate(passport.getIssueDate()))
            return "Дата выдачи должна быть в формате YYYY-MM-DD.";
        if (passport.getIssuedBy() == null || passport.getIssuedBy().isEmpty())
            return "Поле \"Кем выдан\" не может быть пустым.";
        if (passport.getDepartmentCode() == null || !Pattern.matches("\\d{3}-\\d{3}", passport.getDepartmentCode()))
            return "Код подразделнеия должен быть в формате XXX-XXX.";
        if (passport.getRegistrationPlace() == null || passport.getRegistrationPlace().isEmpty())
            return "Место регистрации не может быть пустым.";
        if (passport.getResidencePlace() == null || passport.getResidencePlace().isEmpty())
            return "Место жительства не может быть пустым (обязательное условие для богатого инвестора).";
        return "";
    }

    private String generateWelcomeMessage(Passport passport) {
        String greeting = "Ну привет";
        if (passport.getGender().equals("Мужской")) {
            greeting += ", мистер " + passport.getLastName() + "! Инвестируй в нас, мы очень умные ребята (честно + честно).";
        } else {
            greeting += ", миссис " + passport.getLastName() + "! Инвестируй в нас, мы очень умные ребята (честно + честно).";
        }

        boolean is18yo = passportService.isUser18yo(passport.getUser().getId());
        if (is18yo) {
            greeting += "\n\nДобро пожаловать в ИНВЕСТИЦИИ";
        } else {
            greeting += "\n\nТебе доступ запрещен. Пошел вон отсюда.";
        }

        return greeting;
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
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
    public ResponseEntity<String> updatePassport(@PathVariable Long id, @RequestBody Passport passportDetails) {
        Optional<Passport> passport = passportService.findById(id);
        if (passport.isPresent()) {
            Passport passportToUpdate = passport.get();

            String validationMessage = validatePassportData(passportDetails);
            if (!validationMessage.isEmpty()) {
                return ResponseEntity.badRequest().body(validationMessage);
            }

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

            //Passport updatedPassport = passportService.save(passportToUpdate);

            String welcomeMessage = generateWelcomeMessage(passportToUpdate);
            return ResponseEntity.ok(welcomeMessage);
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
