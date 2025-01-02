package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Message;
import be.projet3.toutouapp.repositories.jpa.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message findById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public List<Message> getMessagesByChatId(Integer chatId) {
        return messageRepository.findByChat_ChatId(chatId);
    }
}
