package com.example.mesapp.controllers;

import com.example.mesapp.dto.ReCaptchaResponseDto;
import com.example.mesapp.models.User;
import com.example.mesapp.services.UserService;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Value("${recaptcha.secret}")
    private String reCaptchaSecret;
    private final static String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam String passwordConfirm,
            @RequestParam("g-recaptcha-response") String reCaptchaResponse,
            @Valid User user,
            BindingResult bindingResult, //посилальна змінна обов'язково має бути над(перед) Model
            Model model
    ) {
        //формуємо повний url
        String urlForReCaptcha = String.format(RECAPTCHA_URL, reCaptchaSecret, reCaptchaResponse);
        //отримуємо JSON об'єкт відповіді reCaptcha через dto-шку
        ReCaptchaResponseDto reCaptchaResponseDto = restTemplate.postForObject(
                urlForReCaptcha, Collections.emptyList(), ReCaptchaResponseDto.class);

        //якщо запит невдалий
        if (!reCaptchaResponseDto.isSuccess()) {
            model.addAttribute("reCaptchaError", "Please, fill reCaptcha.");
        }


        boolean isPasswordConfirmEmpty = StringUtils.isEmpty(passwordConfirm);

        //перевірка для наявності підтвердження паролю користувачем після зміни пароля та пошти, дане поле не являється
        //полем Entity User, тому перевірку виконуємо у ручному режимі
        if (isPasswordConfirmEmpty) {
            model.addAttribute("passwordConfirmError", "Password confirmation can`t be empty!");
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Password is not confirm! Please check your password confirmation.");
        }

        //якщо поле підтвердження пароля пусте або інші провалідовані поля пусті, або reCaptcha не має успішний запит, то
        if (isPasswordConfirmEmpty || bindingResult.hasErrors() || !reCaptchaResponseDto.isSuccess()) {
            Map<String, String> errorsMapFromBindingResult = ControllerUtils.getErrorsMapFromBindingResult(bindingResult);

            model.mergeAttributes(errorsMapFromBindingResult);

            return "registration"; //коли юзер не підтвердив пароль переправляємо його на сторінку реєстрації
        }

        if (!userService.addUser(user)) { //якщо юзера не додано, то
            model.addAttribute("usernameError", "User already exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(
            Model model,
            @PathVariable String code
    ) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Successful user activation!");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
