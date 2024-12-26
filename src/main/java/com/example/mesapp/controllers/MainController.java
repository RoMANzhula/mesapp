package com.example.mesapp.controllers;

import com.example.mesapp.models.Message;
import com.example.mesapp.models.User;
import com.example.mesapp.repositories.MessageRepository;
import com.example.mesapp.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private MessageService messageService;


    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable //пейдженація з дефолтною анотацією - сортуємо за айдішником по у зворотньому напрямку
    ) {
        Page<Message> pageWithMessages = messageService.messageList(pageable, filter); //список для всіх повідомлень

        model.addAttribute("page", pageWithMessages); //додаємо у модель список з повідомлення, для передачі користувачу у вигляді пейдженації
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter); //додаємо в модель фільтр (об'єкт, за яким відбувається сортування на сторінці юзера)

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message, //запускаємо валідацію
            BindingResult bindingResult, //список аргументів та помилок валідації
            Model model,
            @RequestParam("file") MultipartFile file,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable //пейдженація з дефолтною анотацією - сортуємо за айдішником по у зворотньому напрямку

    ) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()) { //якщо "обов'язковий результат" матиме помилки у валідації, то
            Map<String, String> errorsMap = ControllerUtils.getErrorsMapFromBindingResult(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);

        } else {

            saveFile(message, file);

            model.addAttribute("message", null); //скидаємо заповнені строки попереднього повідомлення

            messageRepository.save(message); //через інтерфейс Репозиторіїв ми зберігаємо повідомлення до БД

        }
        model.addAttribute("url", "/main");
        Page<Message> page = this.messageService.messageList(pageable, ""); // пустой фильтр
        model.addAttribute("page", page);

        return "main";
    }

    private void saveFile(Message message, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uniqName = UUID.randomUUID().toString();
            String resultFileName = uniqName + "." + file.getOriginalFilename();

            //загружаємо файл
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author, //ім'я змінної співпадає з ім'ям у @GetMapping, інакше довелось би робити так @PathVariable(name = "name") User anotherName
            Model model,
            @RequestParam(required = false) Message message, //required = false - для виключення помилки: There was an unexpected error (type=Bad Request, status=400).Required parameter 'id' is not present.
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable //пейдженація з дефолтною анотацією - сортуємо за айдішником по у зворотньому напрямку

    ) {
        Page<Message> pageWithMessages = messageService.messageListForUser(pageable, author);

        model.addAttribute("userChanel", author); //атрибут для передачі користувача, чию сторінку ми відкриваємо
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size()); //повертаємо кількість підписок
        model.addAttribute("subscribersCount", author.getSubscribers().size()); //кількість підписників
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser)); //беремо поточного юзера до якого ми прийшли, беремо його підписників і перевіряємо чи є в списку поточний користувач
        model.addAttribute("page", pageWithMessages); //return to user all its messages
        model.addAttribute("message", message); //для можливості редагувати повідомлення на сторінці редагування (поля тепер підтягуватись з повідомлення, яке ми хочемо редагувати)
        model.addAttribute("isCurrentUser", currentUser.equals(author)); //for this we need to overload methods equals and hashCode in User class by field "long Id"
        model.addAttribute("url", "/user-message/" + author.getId());

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMyMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }

            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }

            saveFile(message, file);

            messageRepository.save(message);
        }

        return "redirect:/user-messages/" + user;
    }

    @GetMapping("/del-user-messages/{user}") //контроллер для удаления сообщения
    public String deleteMessage(
            @PathVariable Long user,
            @RequestParam("message") Long messageId
    ) throws IOException {

        messageRepository.deleteById(messageId);

        return "redirect:/user-messages/" + user; //возвращаем пользователя после удаления сообщения на страничку с его сообщениями
    }


}
