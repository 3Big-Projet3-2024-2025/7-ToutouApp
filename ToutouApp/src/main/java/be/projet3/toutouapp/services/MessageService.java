package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Message;
import be.projet3.toutouapp.repositories.jpa.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing {@link Message} entities.
 * Provides methods to save, find and retrieve messages related to chats.
 *
 *@see be.projet3.toutouapp.services
 *@author Jaï Dusépulchre
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Saves a {@link Message} entity in the repository.
     *
     * @param message the message to be saved.
     * @return the saved message.
     */
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    /**
     * Retrieves a {@link Message} by its ID.
     *
     * @param messageId the ID of the message to retrieve.
     * @return the found message, or null if not found.
     */
    public Message findById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    /**
     * Retrieves all messages associated with a specific chat.
     *
     * @param chatId the ID of the chat whose messages are to be retrieved.
     * @return a list of messages belonging to the chat.
     */
    public List<Message> getMessagesByChatId(Integer chatId) {
        return messageRepository.findByChat_ChatId(chatId);
    }
}
