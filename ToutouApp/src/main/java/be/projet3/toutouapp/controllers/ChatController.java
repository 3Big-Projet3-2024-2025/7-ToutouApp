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

/**
 * Controller responsible for managing chat-related operations.
 * Provides endpoints to create and retrieve chats based on associated requests.
 *
 * @see be.projet3.toutouapp.controllers
 * @author Jaï Dusépulchre
 */
@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private RequestService requestService;

    /**
     * Creates a new chat for a specific request.
     *
     * @param requestId the ID of the request for which the chat is to be created
     * @return ResponseEntity containing the created Chat object and HTTP status:
     * - 201 (Created) if the chat is successfully created
     * - 404 (Not Found) if the request does not exist
     * - 500 (Internal Server Error) if an unexpected error occurs
     */
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

    /**
     * Retrieves a chat associated with a specific request ID.
     *
     * @param requestId the ID of the request for which the chat is to be retrieved
     * @return ResponseEntity containing the Chat object and HTTP status:
     * - 200 (OK) if the chat is found
     * - 404 (Not Found) if no chat is associated with the given request ID
     */
    @GetMapping("/chats/request/{requestId}")
    public ResponseEntity<Chat> getChatByRequest(@PathVariable Integer requestId) {
        Optional<Chat> chat = chatService.findByRequestId(requestId);
        return chat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
