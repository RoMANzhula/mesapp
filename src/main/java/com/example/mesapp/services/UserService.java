package com.example.mesapp.services;

import com.example.mesapp.configurations.Encoders;
import com.example.mesapp.models.Role;
import com.example.mesapp.models.User;
import com.example.mesapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private Encoders passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username); //отримуємо ім'я користувача з БД
        if (user == null) {
            throw new UsernameNotFoundException("User with Name - " + username + " not found!");
        }
        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        //якщо юзера не було додано
        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString()); //генеримо код активації для пошти
        user.setPassword(passwordEncoder.userPasswordEncoder().encode(user.getPassword())); //шифруємо пароль при реєстрації

        userRepository.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) { //якщо є пошта (строка не пуста - пошта в нас має вигляд String))
            String text = String.format(
              "Good day, %s! \n " +
                      "Welcome to our big family - Corporation MesApp! \n" +
                      "Please, for agree your registration visit next link: http://localhost:8080/activate/%s", //цю частину на продакшині треба замінити
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSenderService.send(user.getEmail(), "Activation code", text);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null); //юзер підтвердив свій email

        userRepository.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> formRoles) {
        user.setUsername(username); //встановлюємо нове(або лишаться старе) ім'я юзеру

        //переводимо перелік ролей з enum в Set
        Set<String> roles = Arrays
                .stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear(); //очищаємо ролі у користувача перед встановленням нової

        for (String key : formRoles.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key)); //встановлюємо юзеру роль, якщо вона є в списку enum Role
            }
        }

        userRepository.save(user);
    }

    public void updateProfile(User user, String newPassword, String newEmail) {
        String userEmail = user.getEmail();

        boolean isEmailChangedByUser = (newEmail != null && !newEmail.equals(userEmail) ||
                (userEmail != null && !userEmail.equals(newEmail)));

        if (isEmailChangedByUser) {
            user.setEmail(newEmail); //встановлюємо нову пошту

            if (!StringUtils.isEmpty(newEmail)) { //перевіряємо чи установив юзер нову пошту
                user.setActivationCode(UUID.randomUUID().toString()); //посилаємо новий код активації
            }
        }

        if (!StringUtils.isEmpty(newPassword)) { //перевіряємо чи встановив юзер новий пароль
            user.setPassword(newPassword); //встановлюємо новий пароль
        }

        userRepository.save(user); //зберігаємо нові дані в БД

        if (isEmailChangedByUser) { //якщо юзер змінив пошту, то
            sendMessage(user); //відправляємо юзеру повідомлення з кодом активації (після зміни ним його даних)
        }
    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser); //додаємо юзеру до підписників поточного користувача

        userRepository.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser); //видаляємо юзеру з підписників поточного користувача

        userRepository.save(user);
    }
}
