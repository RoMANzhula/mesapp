package com.example.mesapp.controllers;

import com.example.mesapp.models.Role;
import com.example.mesapp.models.User;
import com.example.mesapp.services.UserService;
import org.hibernate.cache.spi.AbstractCacheTransactionSynchronization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')") //перевіряємо наявність прав у користувачів (дозволено лише для ADMIN) в WebSecurityConfig треба додати анотацію @EnableGlobalMethodSecurity
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')") //перевіряємо наявність прав у користувачів (дозволено лише для ADMIN) в WebSecurityConfig треба додати анотацію @EnableGlobalMethodSecurity
    @GetMapping("{user}")
    public String userEditForm(
            @PathVariable User user,
            Model model
    ) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values()); //в модель отримуємо всі значення enum`a Role

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')") //перевіряємо наявність прав у користувачів (дозволено лише для ADMIN) в WebSecurityConfig треба додати анотацію @EnableGlobalMethodSecurity
    @PostMapping
    public String userSaveAfterEdit(
            @RequestParam String username,
            @RequestParam Map<String, String> formRoles,
            @RequestParam("userId") User user
    ) {
        userService.saveUser(user, username, formRoles);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(
            Model model,
            @AuthenticationPrincipal User user //отримуємо користувача з контексту
    ) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
    ) {
        userService.updateProfile(user, password, email);

        return "redirect:/user/profile";
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ) {
        userService.subscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ) {
        userService.unsubscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String userList(
            @PathVariable User user,
            @PathVariable String type,
            Model model
    ) {
        model.addAttribute("userChanel", user);
        model.addAttribute("type", type);

        if ("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }

}
