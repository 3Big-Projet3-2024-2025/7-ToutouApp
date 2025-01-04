package be.projet3.toutouapp;


import be.projet3.toutouapp.controllers.RequestController;
import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.services.IRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RequestControllerTests {

    @InjectMocks
    private RequestController requestController;

    @Mock
    private IRequestService requestService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRequests() {

        Request request1 = new Request();
        request1.setRequestId(1);
        request1.setRequestDate(LocalDate.now());
        request1.setStartTime(LocalTime.of(10, 0));
        request1.setEndTime(LocalTime.of(12, 0));
        request1.setDogName("Buddy");

        Request request2 = new Request();
        request2.setRequestId(2);
        request2.setRequestDate(LocalDate.now());
        request2.setStartTime(LocalTime.of(14, 0));
        request2.setEndTime(LocalTime.of(16, 0));
        request2.setDogName("Max");

        when(requestService.getRequests()).thenReturn(Arrays.asList(request1, request2));


        ResponseEntity<List<Request>> response = requestController.getAllRequests();


        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Buddy", response.getBody().get(0).getDogName());
    }

    @Test
    void testAddRequest() {

        Request request = new Request();
        request.setRequestId(1);
        request.setRequestDate(LocalDate.now());
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setDogName("Buddy");

        when(requestService.addRequest(any(Request.class))).thenReturn(request);


        ResponseEntity<Request> response = requestController.addRequest(request);


        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Buddy", response.getBody().getDogName());
    }

    @Test
    void testUpdateRequest() {

        Request request = new Request();
        request.setRequestId(1);
        request.setRequestDate(LocalDate.now());
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setDogName("Buddy Updated");

        when(requestService.updateRequest(any(Request.class))).thenReturn(request);


        ResponseEntity<Request> response = requestController.updateRequest(1, request);


        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Buddy Updated", response.getBody().getDogName());
    }

    @Test
    void testDeleteRequest() {
        // Arrange
        doNothing().when(requestService).deleteRequest(1);


        ResponseEntity<Void> response = requestController.deleteRequest(1);


        assertEquals(204, response.getStatusCodeValue());
        verify(requestService, times(1)).deleteRequest(1);
    }

    @Test
    void testGetRequestById() {

        Request request = new Request();
        request.setRequestId(1);
        request.setRequestDate(LocalDate.now());
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setDogName("Buddy");

        when(requestService.getRequestById(1)).thenReturn(request);


        Request result = requestController.getRequestById(1);


        assertNotNull(result);
        assertEquals("Buddy", result.getDogName());
    }

}
