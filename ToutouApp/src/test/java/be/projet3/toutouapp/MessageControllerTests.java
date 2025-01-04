package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.MessageController;
import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Message;
import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.services.MessageService;
import be.projet3.toutouapp.services.ChatService;
import be.projet3.toutouapp.services.UserService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * These tests validate the functionality of sending and retrieving messages from a chat.
 *
 * @see be.projet3.toutouapp
 * @author Jaï Dusépulchre
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    /**
     * Initializes mocks and sets up the test data before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialization of the objects necessary for the tests
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

    /**
     * Tests the successful sending of a message when chat exists and user is allowed to send a message.
     */
    @Test
    @Order(1)
    @DisplayName("Test successful sending of a message")
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

    /**
     * Tests the case where the chat was not found when sending a message.
     */
    @Test
    @Order(2)
    @DisplayName("Test sending a message when chat is not found")
    void testSendMessage_ChatNotFound() {
        when(chatService.findByChatId(1)).thenReturn(Optional.empty());

        ResponseEntity<Message> response = messageController.sendMessage(1, message);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(chatService, times(1)).findByChatId(1);
        verify(messageService, never()).saveMessage(message);
    }

    /**
     * Tests the scenario where the user is forbidden from sending a message due to a service exception.
     */
    @Test
    @Order(3)
    @DisplayName("Test sending a message when user is forbidden")
    void testSendMessage_Forbidden() {
        when(chatService.findByChatId(1)).thenReturn(Optional.of(chat));
        when(userService.getCurrentUser()).thenThrow(new RuntimeException("Forbidden"));

        ResponseEntity<Message> response = messageController.sendMessage(1, message);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(chatService, times(1)).findByChatId(1);
        verify(userService, times(1)).getCurrentUser();
        verify(messageService, never()).saveMessage(message);
    }

    /**
     * Tests the successful retrieval of messages by chat ID.
     */
    @Test
    @Order(4)
    @DisplayName("Test successful retrieval of messages by chat ID")
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

    /**
     * Tests the scenario where no messages are found for the specified chat ID.
     */
    @Test
    @Order(5)
    @DisplayName("Test retrieval of messages by chat ID when no messages are found")
    void testGetMessagesByChatId_NoMessages() {
        when(messageService.getMessagesByChatId(1)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Message>> response = messageController.getMessagesByChatId(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(messageService, times(1)).getMessagesByChatId(1);
    }

    /**
     * Tests the successful operation of marking a message as read.
     */
    @Test
    @Order(6)
    @DisplayName("Test marking a message as read successfully")
    void testMarkAsRead_Success() {
        when(messageService.findById(1)).thenReturn(message);
        when(messageService.saveMessage(message)).thenReturn(message);

        ResponseEntity<Message> response = messageController.markAsRead(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getReaded());
        verify(messageService, times(1)).findById(1);
        verify(messageService, times(1)).saveMessage(message);
    }


    /**
     * Tests the case when trying to mark a message as read but the message was not found.
     */
    @Test
    @Order(7)
    @DisplayName("Test marking a message as read when the message is not found")
    void testMarkAsRead_MessageNotFound() {
        when(messageService.findById(1)).thenReturn(null);

        ResponseEntity<Message> response = messageController.markAsRead(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(messageService, times(1)).findById(1);
        verify(messageService, never()).saveMessage(any());
    }
}
