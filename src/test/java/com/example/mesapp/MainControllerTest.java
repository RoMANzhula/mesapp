package com.example.mesapp;


import com.example.mesapp.controllers.MainController;
import com.example.mesapp.models.Message;
import com.example.mesapp.repositories.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MesappApplication.class})
@AutoConfigureMockMvc
@WithUserDetails(value = "admin") //анотація для роботи з конкретним авторизованим користувачем
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/message-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void mainPageTest() throws Exception {
        this.mockMvc
                .perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='navbarSupportedContent']/div").string("Login or registration"));
    }

    @Test
    public void messageListTest() throws Exception {
        this.mockMvc
                .perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(4));

    }

    @Test
    public void filterMessageTest() throws Exception {
        this.mockMvc
                .perform(get("/main").param("filter", "tag-1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(2))
                .andExpect(xpath("//*[@id='message-list']/div/div[@data-id=1]").exists())
                .andExpect(xpath("//*[@id='message-list']/div/div[@data-id=3]").exists());
    }


    @Test
    public void addMessageToListTest() throws Exception {
        byte[] fileContent = "12113".getBytes();
        MockHttpServletRequestBuilder multipartFile = multipart("/main")
                .file("file", fileContent)
                .param("text", "fifth")
                .param("tag", "new one")
                .with(csrf());

        this.mockMvc
                .perform(multipartFile)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(5)) // Перевірка наявності п'яти елементів у списку
                .andExpect(xpath("//*[contains(text(),'fifth')]").exists()) // Перевірка наявності тексту "fifth"
                .andExpect(xpath("//*[contains(text(),'##new one')]").exists()) // Перевірка наявності тексту "new one"
        ;


//        this.mockMvc
//                .perform(multipartFile)
//                .andDo(print())
//                .andExpect(authenticated())
//                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(5)) // Перевірка наявності п'яти елементів у списку
//                .andExpect(xpath("//*[@id='message-list']/div['10']/div").exists())
//                .andExpect(xpath("//*[@id='message-list']/div['10']/div/div[2]/span").string("fifth")) // Перевірка тексту
//                .andExpect(xpath("//*[@id='message-list']/div['10']/div/div[2]/i").string("##new one")) // Перевірка тегу
//        ;
    }


    @Test
    public void testAddMessageToList2() throws Exception {
        byte[] fileContent = "12113".getBytes(); // Замініть це на вміст вашого файлу
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", fileContent);

        // Створюємо нове повідомлення, яке буде додано до списку
        Message newMessage = new Message();
        newMessage.setText("New message text");
        newMessage.setTag("New message tag");

        mockMvc.perform(multipart("/main")
                        .file(file) // Надсилаємо файл
                        .param("text", newMessage.getText()) // Передаємо текст повідомлення
                        .param("tag", newMessage.getTag()) // Передаємо тег повідомлення
                        .with(csrf()))
                .andExpect(status().isOk());

        //тепер перевіримо, чи створилося нове повідомлення в базі даних
        Message createdMessage = messageRepository.findByText(newMessage.getText());

        //перевіримо, чи створене повідомлення не є порожнім і відповідає переданим даним
        assertNotNull(createdMessage);
        assertEquals(newMessage.getText(), createdMessage.getText());
        assertEquals(newMessage.getTag(), createdMessage.getTag());
    }
}
