package be.projet3.toutouapp.controllers;

import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Message;
import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.services.MessageService;
import be.projet3.toutouapp.services.UserService;
import be.projet3.toutouapp.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @PostMapping("/send/{chatId}")
    public ResponseEntity<Message> sendMessage(@PathVariable Integer chatId, @RequestBody Message message) {
        Optional<Chat> chatOptional = chatService.findByChatId(chatId);
        if (chatOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Chat chat = chatOptional.get();

        User consumer;
        try {
            consumer = userService.getCurrentUser();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        message.setChat(chat);
        message.setConsumer(consumer);

        try {
            Message savedMessage = messageService.saveMessage(message);
            return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Integer chatId) {
        List<Message> messages = messageService.getMessagesByChatId(chatId);
        if (messages == null || messages.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


    @PatchMapping("/read/{messageId}")
    public ResponseEntity<Message> markAsRead(@PathVariable Integer messageId) {
        Message message = messageService.findById(messageId);
        if (message == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        message.setReaded(true);
        Message updatedMessage = messageService.saveMessage(message);

        return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
    }
}
