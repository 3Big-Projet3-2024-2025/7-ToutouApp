package be.projet3.toutouapp.controllers;


import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.repositories.jpa.RequestRepository;
import be.projet3.toutouapp.services.IRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing requests in the application.
 * This class provides the endpoints for interacting with requests, including retrieving, creating, updating, and deleting requests.
 *
 * @author Amico Matteo
 */

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private IRequestService requestService;



    /**
     * Retrieves all requests.
     * This endpoint returns a list of all requests with a 200 OK status.
     *
     * @return A ResponseEntity containing the list of requests and HTTP status 200.
     * @author Amico Matteo
     */
    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getRequests();
        return ResponseEntity.ok(requests); // Retourne une réponse HTTP 200 avec les données
    }

    /**
     * Adds a new request.
     * This endpoint creates a new request and returns the created request with a 201 Created status.
     *
     * @param request The request to be added.
     * @return A ResponseEntity containing the newly created request and HTTP status 201.
     * @author Amico Matteo
     */
    @PostMapping
    public ResponseEntity<Request> addRequest(@RequestBody Request request) {
        Request newRequest = requestService.addRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRequest); // HTTP 201 Created
    }

    /**
     * Updates an existing request by ID.
     * This endpoint updates the request identified by the provided ID and returns the updated request with a 200 OK status.
     *
     * @param id The ID of the request to be updated.
     * @param request The updated request data.
     * @return A ResponseEntity containing the updated request and HTTP status 200.
     * @author Amico Matteo
     */
    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(@PathVariable int id, @RequestBody Request request) {

        request.setRequestId(id);
        Request updatedRequest = requestService.updateRequest(request);
        return ResponseEntity.ok(updatedRequest); // HTTP 200 OK
    }

    /**
     * Deletes a request by ID.
     * This endpoint deletes the request identified by the provided ID and returns HTTP 204 No Content status.
     *
     * @param id The ID of the request to be deleted.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     * @author Amico Matteo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable int id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    /**
     * Retrieves all requests for a specific user.
     * This endpoint returns a list of requests associated with the specified user ID.
     *
     * @param userId The ID of the user whose requests are to be retrieved.
     * @return A list of requests for the specified user.
     * @author Amico Matteo
     */
    @GetMapping("/user/{userId}")
    public List<Request> getRequestsByUserId(@PathVariable int userId) {
        return requestService.getRequestsByUserId(userId);
    }

    /**
     * Retrieves a request by its ID.
     * This endpoint returns the request with the specified ID.
     *
     * @param id The ID of the request to be retrieved.
     * @return The request with the specified ID.
     * @author Amico Matteo
     */
    @GetMapping("/{id}")
    public Request getRequestById(@PathVariable int id) {
        return requestService.getRequestById(id);
    }

}
