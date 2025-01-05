package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.repositories.jpa.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Service class for managing Request entities.
 * Implements the {@link IRequestService} interface to provide business logic for Request operations.
 * Annotated with {@link Service} to indicate it's a Spring service component.
 * @see be.projet3.toutouapp.services
 * @author Amico Matteo
 */
@Service
public class RequestService implements IRequestService{


    /**
     * Repository for performing CRUD operations on Request entities.
     * Automatically injected by Spring using the {@link Autowired} annotation.
     * @author Amico Matteo
     */
    @Autowired
    private RequestRepository requestRepository;


    /**
     * Retrieves all Request entities from the database.
     *
     * @return a list of all {@link Request} entities
     * @author Amico Matteo
     */
    @Override
    public List<Request> getRequests() {
        return requestRepository.findAll();
    }

    /**
     * Adds a new Request entity to the database.
     *
     * @param request the {@link Request} entity to be added
     * @return the saved {@link Request} entity
     * @author Amico Matteo
     */
    @Override
    public Request addRequest(Request request) {
        return requestRepository.save(request);
    }

    /**
     * Updates an existing Request entity in the database.
     * Throws an exception if the Request with the given ID does not exist.
     *
     * @param request the {@link Request} entity to be updated
     * @return the updated {@link Request} entity
     * @throws RuntimeException if the Request with the specified ID does not exist
     * @author Amico Matteo
     */
    @Override
    public Request updateRequest(Request request) {
        if (!requestRepository.existsById(request.getRequestId())) {
            throw new RuntimeException("La requête avec l'ID " + request.getRequestId() + " n'existe pas.");
        }
        return requestRepository.save(request);
    }

    /**
     * Deletes a Request entity with the specified ID from the database.
     * If no entity with the given ID exists, the method does nothing.
     *
     * @param id the ID of the {@link Request} entity to be deleted
     * @author Amico Matteo
     */
    @Override
    public void deleteRequest(int id) {
        if (requestRepository.existsById(id)) {
            requestRepository.deleteById(id);
        }

    }

    /**
     * Retrieves all Request entities associated with a specific user ID.
     *
     * @param userId the ID of the user whose requests are to be retrieved
     * @return a list of {@link Request} entities belonging to the specified user
     * @author Amico Matteo
     */
    @Override
    public List<Request> getRequestsByUserId(int userId) {
        return requestRepository.findByOwner_Id(userId);
    }

    /**
     * Retrieves a Request entity with the specified ID.
     *
     * @param id the unique ID of the {@link Request} to be retrieved
     * @return the {@link Request} entity with the specified ID
     * @author Amico Matteo
     */
    @Override
    public Request getRequestById(int id) {
        return requestRepository.findByRequestId(id);
    }

    /**
     * Finds a Request entity by its ID, wrapped in an {@link Optional}.
     *
     * @param requestId the unique ID of the {@link Request} to be found
     * @return an {@link Optional} containing the {@link Request} entity, or empty if not found
     * @author Amico Matteo
     */
    public Optional<Request> findById(Integer requestId) {
        return requestRepository.findById(requestId);
    }
}
