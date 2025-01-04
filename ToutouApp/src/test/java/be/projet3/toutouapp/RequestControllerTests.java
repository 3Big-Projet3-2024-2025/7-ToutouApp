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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RequestControllerTests {

    @InjectMocks
    private RequestController requestController;

    @Mock
    private IRequestService requestService;

    /**
     * Setup method to initialize mocks before each test.
     * This is run before each test method to prepare the test environment.
     *
     * @author Amico Matteo
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the {@link RequestController#getAllRequests()} method.
     * This test verifies that the controller returns a list of requests with a 200 HTTP status code.
     * @author Amico Matteo
     */
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

    /**
     * Test the {@link RequestController#addRequest(Request)} method.
     * This test checks if a new request is correctly added and returns a 201 status code.
     * @author Amico Matteo
     */
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

    /**
     * Test the {@link RequestController#updateRequest(int, Request)} method.
     * This test checks if a request can be updated and returns the updated request with a 200 status code.
     * @author Amico Matteo
     */
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

    /**
     * Test the {@link RequestController#deleteRequest(int)} method.
     * This test verifies if a request can be deleted and returns a 204 status code when successful.
     * @author Amico Matteo
     */
    @Test
    void testDeleteRequest() {
        // Arrange
        doNothing().when(requestService).deleteRequest(1);

        ResponseEntity<Void> response = requestController.deleteRequest(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(requestService, times(1)).deleteRequest(1);
    }

    /**
     * Test the {@link RequestController#getRequestById(int)} method.
     * This test checks if a request can be retrieved by its ID and the correct request is returned.
     * @author Amico Matteo
     */
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

    /**
     * Test case where no requests exist in the system.
     * This test verifies that the response is empty and has a 200 status code when no requests are found.
     * @author Amico Matteo
     */
    @Test
    void testGetAllRequestsEmpty() {
        // Arrange
        when(requestService.getRequests()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Request>> response = requestController.getAllRequests();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());
    }

    /**
     * Test case for getting a request by an ID that doesn't exist.
     * This test checks that when a request ID does not exist, the method should return null.
     * @author Amico Matteo
     */
    @Test
    void testGetRequestByIdNotFound() {
        // Arrange
        when(requestService.getRequestById(999)).thenReturn(null); // Simulate non-existing ID

        // Act
        Request result = requestController.getRequestById(999); // Non-existent ID

        // Assert
        assertNull(result); // Expecting null as the request doesn't exist
    }

    /**
     * Test case for deleting a request that doesn't exist.
     * This test checks if the system correctly handles the case when trying to delete a non-existent request.
     * @author Amico Matteo
     */
    @Test
    void testDeleteRequestNotFound() {
        // Arrange
        doThrow(new RuntimeException("Request not found")).when(requestService).deleteRequest(999); // Simulate exception

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            requestController.deleteRequest(999); // Non-existent ID
        });

        // Assert
        assertEquals("Request not found", exception.getMessage()); // Expecting a custom exception message
    }

    /**
     * Test the {@link RequestController#getRequestsByUserId(int)} method.
     * This test checks if the system correctly retrieves requests by a specific user ID.
     * @author Amico Matteo
     */
    @Test
    void testGetRequestsByUserId() {
        // Arrange
        int userId = 1;


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

        when(requestService.getRequestsByUserId(userId)).thenReturn(Arrays.asList(request1, request2));

        // Act
        List<Request> response = requestController.getRequestsByUserId(userId);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Buddy", response.get(0).getDogName());
        assertEquals("Max", response.get(1).getDogName());
    }


}
