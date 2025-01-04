package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.ChatController;
import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.services.ChatService;
import be.projet3.toutouapp.services.RequestService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * These tests validate the creation of a chat, retrieving chat by request ID,
 * handling not found errors, and internal server errors for chat-related API endpoints.
 *
 * @see be.projet3.toutouapp
 * @author Jaï Dusépulchre
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChatControllerTests {

    @InjectMocks
    private ChatController chatController;

    @Mock
    private ChatService chatService;

    @Mock
    private RequestService requestService;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests successful creation of a chat when the request exists.
     */
    @Test
    @Order(1)
    @DisplayName("Test successful chat creation when request exists")
    public void testCreateChatSuccess() {
        Integer requestId = 1;
        Request mockRequest = new Request();
        mockRequest.setRequestId(requestId);

        when(requestService.findById(requestId)).thenReturn(Optional.of(mockRequest));

        Chat mockChat = new Chat();
        mockChat.setChatId(1);
        mockChat.setRequest(mockRequest);

        when(chatService.save(any(Chat.class))).thenReturn(mockChat);

        ResponseEntity<Chat> response = chatController.createChat(requestId);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(mockRequest, response.getBody().getRequest());
    }

    /**
     * Tests the case when the request does not exist, resulting in a 404 error.
     */
    @Test
    @Order(2)
    @DisplayName("Test chat creation with non-existent request (404 Not Found)")
    public void testCreateChatRequestNotFound() {
        Integer requestId = 1;
        when(requestService.findById(requestId)).thenReturn(Optional.empty());

        ResponseEntity<Chat> response = chatController.createChat(requestId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    /**
     * Tests the scenario when an internal server error occurs during chat creation.
     * This is simulated by throwing an exception in the request service.
     */
    @Test
    @Order(3)
    @DisplayName("Test internal server error during chat creation")
    public void testCreateChatInternalServerError() {
        Integer requestId = 1;
        when(requestService.findById(requestId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Chat> response = chatController.createChat(requestId);

        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    /**
     * Tests the successful retrieval of a chat by request ID.
     */
    @Test
    @Order(4)
    @DisplayName("Test successful retrieval of chat by request ID")
    public void testGetChatByRequestSuccess() {
        Integer requestId = 1;
        Chat mockChat = new Chat();
        mockChat.setChatId(1);

        when(chatService.findByRequestId(requestId)).thenReturn(Optional.of(mockChat));

        ResponseEntity<Chat> response = chatController.getChatByRequest(requestId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(mockChat, response.getBody());
    }

    /**
     * Tests the case when no chat is found for the given request ID, resulting in a 404 error.
     */
    @Test
    @Order(5)
    @DisplayName("Test chat retrieval with non-existent request ID (404 Not Found)")
    public void testGetChatByRequestNotFound() {
        Integer requestId = 1;
        when(chatService.findByRequestId(requestId)).thenReturn(Optional.empty());

        ResponseEntity<Chat> response = chatController.getChatByRequest(requestId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
