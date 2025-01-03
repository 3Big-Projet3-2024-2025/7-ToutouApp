package be.projet3.toutouapp.controllers;

import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Participant;
import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.models.User;

import be.projet3.toutouapp.repositories.jpa.ChatRepository;
import be.projet3.toutouapp.repositories.jpa.ParticipantRepository;
import be.projet3.toutouapp.repositories.jpa.RequestRepository;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import be.projet3.toutouapp.services.ChatService;
import be.projet3.toutouapp.services.RequestService;
import be.projet3.toutouapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private RequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<Chat> createChat(@RequestParam Integer requestId) {
        try {
            Optional<Request> requestOptional = requestService.findById(requestId);
            if (!requestOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Request request = requestOptional.get();

            Chat newChat = new Chat();
            newChat.setRequest(request);

            Chat savedChat = chatService.save(newChat);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedChat);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/chats/request/{requestId}")
    public ResponseEntity<Chat> getChatByRequest(@PathVariable Integer requestId) {
        Optional<Chat> chat = chatService.findByRequestId(requestId);
        return chat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
