package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.ChatController;
import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.services.ChatService;
import be.projet3.toutouapp.services.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatControllerTests {

    @InjectMocks
    private ChatController chatController;

    @Mock
    private ChatService chatService;

    @Mock
    private RequestService requestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
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

    @Test
    public void testCreateChatRequestNotFound() {
        Integer requestId = 1;
        when(requestService.findById(requestId)).thenReturn(Optional.empty());

        ResponseEntity<Chat> response = chatController.createChat(requestId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateChatInternalServerError() {
        Integer requestId = 1;
        when(requestService.findById(requestId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Chat> response = chatController.createChat(requestId);

        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
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

    @Test
    public void testGetChatByRequestNotFound() {
        Integer requestId = 1;
        when(chatService.findByRequestId(requestId)).thenReturn(Optional.empty());

        ResponseEntity<Chat> response = chatController.getChatByRequest(requestId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
