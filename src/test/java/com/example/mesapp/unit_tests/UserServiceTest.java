package com.example.mesapp.unit_tests;

import com.example.mesapp.models.Role;
import com.example.mesapp.models.User;
import com.example.mesapp.repositories.UserRepository;
import com.example.mesapp.services.MailSenderService;
import com.example.mesapp.services.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailSenderService mailSenderService;

    @Test
    void addUser() {
        User user = new User();

        user.setEmail("some@mail.net");

        boolean isUserExists = userService.addUser(user);

        //перевіряємо, чи успішно було створено нового юзера
        Assert.assertTrue(isUserExists);
        //тепер перевіряємо код активації що він не null
        Assert.assertNotNull(user.getActivationCode());
        //тепер перевіримо роль юзера, використавши сінглтон
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        //перевірка на те, що юзерРепо був викликан один раз при збереженні юзера - тобто перевіряємо збереження юзера в БД
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        //перевіряємо відправку листа на пошту при реєстрації
        Mockito.verify(mailSenderService, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.eq("Activation code"),
                        ArgumentMatchers.contains("Good day,")
//                        ArgumentMatchers.anyString() //коли нам не важливо яка має бути строка для перевірки, то використовуємо цей рядок
                );
    }

    @Test
    public void addUserFalseTest() {
        User user = new User();

        user.setUsername("Mark");

        //предінсталяція юзера з ім'ям, інакше тест буде провалюватися т.к. не має де взяти буде такого юзера
        Mockito.doReturn(new User())
                .when(userRepository)
                .findByUsername("Mark");

        boolean isUserExists = userService.addUser(user);

        //перевіряємо дублювання юзера і отримуємо false, т.я. юзер з таким ім'ям вже існує
        Assert.assertFalse(isUserExists);
        //перевіряємо, що не було звернення до юзерРепо для збереження будь-якого об'кту класу User
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSenderService, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(), //коли нам не важливо яка має бути строка для перевірки, то використовуємо цей рядок
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void activateUser() {
        User user = new User();

        user.setActivationCode("success!");

        //предінсталяція юзера з кодом активації, інакше тест буде провалюватися т.к. не має де взяти буде такого юзера
        Mockito.doReturn(user)
                .when(userRepository)
                .findByActivationCode("activ");

        boolean isUserActivated = userService.activateUser("activ");

        //перевірка чи активован юзер
        Assert.assertTrue(isUserActivated);
        //чи пусте поле активаційного коду
        Assert.assertNull(user.getActivationCode());

        //перевірка на те, що юзерРепо був викликан один раз при збереженні юзера - тобто перевіряємо збереження юзера в БД
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFalse() {
        boolean isUserActivated = userService.activateUser("activ me");

        //перевірка чи НЕ активован юзер, перевірка на false
        Assert.assertFalse(isUserActivated);

        //перевіряємо, що не було звернення до юзерРепо для збереження будь-якого об'кту класу User
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}