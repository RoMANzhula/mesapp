package com.example.mesapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please fill the message!")
    @Length(max = 4096, message = "Message too long. Max length must be less than 4096 characters.")
    private String text;
    @Length(max = 255, message = "Tag too long. Max length must be less than 255 characters.")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename; //має тип строки, т.я. файли будуть зберігатися на жорсткому диску і за ім'ям будемо їх знаходити

    //створюємо обов'язковий пустий конструктор, для правильної роботи Spring
    public Message() {
    }

    //перевіряємо чи є автор у повідомлення
    public String getAuthorName() {
        return author != null ? author.getUsername() : "<without_author>";
    }

    public Message(String text, String tag, User user) {
        this.text = text;
        this.tag = tag;
        this.author = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
