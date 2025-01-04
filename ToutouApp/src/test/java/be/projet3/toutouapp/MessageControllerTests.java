package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.MessageController;
import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Message;
import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.services.MessageService;
import be.projet3.toutouapp.services.ChatService;
import be.projet3.toutouapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageControllerTests {

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageService messageService;

    @Mock
    private ChatService chatService;

    @Mock
    private UserService userService;

    private Chat chat;
    private User user;
    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialisation des objets nécessaires pour les tests
        chat = new Chat();
        chat.setChatId(1);

        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");

        message = new Message();
        message.setMessageId(1);
        message.setContent("Hello World!");
        message.setReaded(false);
        message.setChat(chat);
        message.setConsumer(user);
    }

    @Test
    void testSendMessage_Success() {
        when(chatService.findByChatId(1)).thenReturn(Optional.of(chat));
        when(userService.getCurrentUser()).thenReturn(user);
        when(messageService.saveMessage(message)).thenReturn(message);

        ResponseEntity<Message> response = messageController.sendMessage(1, message);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(message, response.getBody());
        verify(chatService, times(1)).findByChatId(1);
        verify(userService, times(1)).getCurrentUser();
        verify(messageService, times(1)).saveMessage(message);
    }

    @Test
    void testSendMessage_ChatNotFound() {
        when(chatService.findByChatId(1)).thenReturn(Optional.empty());

        ResponseEntity<Message> response = messageController.sendMessage(1, message);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(chatService, times(1)).findByChatId(1);
        verify(messageService, never()).saveMessage(message);
    }

    @Test
    void testSendMessage_Forbidden() {
        when(chatService.findByChatId(1)).thenReturn(Optional.of(chat));
        when(userService.getCurrentUser()).thenThrow(new RuntimeException("Forbidden"));

        ResponseEntity<Message> response = messageController.sendMessage(1, message);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(chatService, times(1)).findByChatId(1);
        verify(userService, times(1)).getCurrentUser();
        verify(messageService, never()).saveMessage(message);
    }

    @Test
    void testGetMessagesByChatId_Success() {
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        when(messageService.getMessagesByChatId(1)).thenReturn(messages);

        ResponseEntity<List<Message>> response = messageController.getMessagesByChatId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains(message));
        verify(messageService, times(1)).getMessagesByChatId(1);
    }

    @Test
    void testGetMessagesByChatId_NoMessages() {
        when(messageService.getMessagesByChatId(1)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Message>> response = messageController.getMessagesByChatId(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(messageService, times(1)).getMessagesByChatId(1);
    }

    @Test
    void testMarkAsRead_Success() {
        when(messageService.findById(1)).thenReturn(message);
        when(messageService.saveMessage(message)).thenReturn(message);

        ResponseEntity<Message> response = messageController.markAsRead(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getReaded());
        verify(messageService, times(1)).findById(1);
        verify(messageService, times(1)).saveMessage(message);
    }

    @Test
    void testMarkAsRead_MessageNotFound() {
        when(messageService.findById(1)).thenReturn(null);

        ResponseEntity<Message> response = messageController.markAsRead(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(messageService, times(1)).findById(1);
        verify(messageService, never()).saveMessage(any());
    }
}
