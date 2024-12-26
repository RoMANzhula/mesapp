package com.example.mesapp.repositories;

import com.example.mesapp.models.Message;
import com.example.mesapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


public interface MessageRepository extends CrudRepository<Message, Long> {
     Page<Message> findAll(Pageable pageable);

     Message findByText(String text);

     Page<Message> findByTag(String filter, Pageable pageable);

     @Query("from Message as m where m.author = :author")
     Page<Message> findByUser(Pageable pageable, @Param("author") User author);
}
