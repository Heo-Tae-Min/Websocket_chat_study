package com.taemin.chat.repository;

import com.taemin.chat.domain.ChatDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<ChatDTO, String> {

}
