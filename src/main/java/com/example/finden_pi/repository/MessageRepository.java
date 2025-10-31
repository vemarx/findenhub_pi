package com.example.finden_pi.repository;

import com.example.finden_pi.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findBySenderIdOrReceiverId(String senderId, String receiverId);

    List<Message> findByReceiverIdAndReadFalse(String receiverId);

    List<Message> findByServiceId(String serviceId);

    Long countByReceiverIdAndReadFalse(String receiverId);
}