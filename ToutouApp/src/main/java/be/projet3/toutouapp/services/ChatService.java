package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.repositories.jpa.ChatRepository;
import be.projet3.toutouapp.repositories.jpa.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides methods to interact with the {@link ChatRepository} and {@link RequestRepository}.
 *
 * @see be.projet3.toutouapp.services
 * @author Jaï Dusépulchre
 */
@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private RequestRepository requestRepository;

    /**
     * Saves a {@link Chat} entity to the database.
     *
     * @param chat the {@link Chat} entity to be saved.
     * @return the saved {@link Chat} entity.
     */
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    /**
     * Finds a {@link Request} by its ID.
     *
     * @param requestId the ID of the request.
     * @return an {@link Optional} containing the {@link Request} if found, or empty if not found.
     */
    public Optional<Request> findById(Integer requestId) {
        return requestRepository.findById(requestId);
    }

    /**
     * Finds a {@link Chat} by the associated {@link Request} ID.
     *
     * @param requestId the ID of the request.
     * @return an {@link Optional} containing the {@link Chat} if found, or empty if not found.
     */
    public Optional<Chat> findByRequestId(Integer requestId) {
        return chatRepository.findByRequest_RequestId(requestId);
    }

    /**
     * Creates and saves a new {@link Chat} entity.
     *
     * @param chat the {@link Chat} entity to be created.
     * @return the created and saved {@link Chat} entity.
     */
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    /**
     * Finds a {@link Chat} by its ID.
     *
     * @param chatId the ID of the chat.
     * @return an {@link Optional} containing the {@link Chat} if found, or empty if not found.
     */
    public Optional<Chat> findByChatId(Integer chatId) {
        return chatRepository.findById(chatId);
    }
}
