package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.repositories.jpa.ChatRepository;
import be.projet3.toutouapp.repositories.jpa.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private RequestRepository requestRepository;

    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    public Optional<Request> findById(Integer requestId) {
        return requestRepository.findById(requestId);
    }

    public Optional<Chat> findByRequestId(Integer requestId) {
        return chatRepository.findByRequest_RequestId(requestId);
    }

    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Optional<Chat> findByChatId(Integer chatId) {
        return chatRepository.findById(chatId);
    }
}
