package com.example.mesapp.services;

import com.example.mesapp.models.Message;
import com.example.mesapp.models.User;
import com.example.mesapp.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    public Page<Message> messageList(Pageable pageable, String filter) {
        if (filter != null && !filter.isEmpty()) {
            return messageRepository.findByTag(filter, pageable);
        } else {
            return messageRepository.findAll(pageable);
        }
    }

    public Page<Message> messageListForUser(Pageable pageable, User author) {
        return messageRepository.findByUser(pageable, author);
    }

//    public List<Message> messageList() {
//
//    }
}
