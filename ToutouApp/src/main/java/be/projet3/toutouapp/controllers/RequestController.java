package be.projet3.toutouapp.controllers;


import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.repositories.jpa.RequestRepository;
import be.projet3.toutouapp.services.IRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private IRequestService requestService;

    // Récupérer toutes les requêtes
    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getRequests();
        return ResponseEntity.ok(requests); // Retourne une réponse HTTP 200 avec les données
    }

    // Ajouter une nouvelle requête
    @PostMapping
    public ResponseEntity<Request> addRequest(@RequestBody Request request) {
        Request newRequest = requestService.addRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRequest); // HTTP 201 Created
    }

    // Mettre à jour une requête existante
    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(@PathVariable int id, @RequestBody Request request) {
        // Associe l'ID reçu dans l'URL à la requête avant de la passer au service
        request.setRequestId(id);
        Request updatedRequest = requestService.updateRequest(request);
        return ResponseEntity.ok(updatedRequest); // HTTP 200 OK
    }

    // Supprimer une requête par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable int id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    @GetMapping("/user/{userId}")
    public List<Request> getRequestsByUserId(@PathVariable int userId) {
        return requestService.getRequestsByUserId(userId);
    }
    @GetMapping("/{id}")
    public Request getRequestById(@PathVariable int id) {
        return requestService.getRequestById(id);
    }
}
