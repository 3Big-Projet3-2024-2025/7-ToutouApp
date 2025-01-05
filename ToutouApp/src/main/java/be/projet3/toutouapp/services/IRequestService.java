package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Request;

import java.util.List;

/**
 * Interface for managing Request entities.
 * Defines the contract for business logic operations related to Requests.
 * @see be.projet3.toutouapp.services
 * @author Amico Matteo
 */
public interface IRequestService {

    /**
     * Retrieves all Request entities from the database.
     *
     * @return a list of all {@link Request} entities
     * @author Amico Matteo
     */
public List<Request> getRequests();

    /**
     * Adds a new Request entity to the database.
     *
     * @param request the {@link Request} entity to be added
     * @return the saved {@link Request} entity
     * @author Amico Matteo
     */
public Request  addRequest(Request request);

    /**
     * Updates an existing Request entity in the database.
     *
     * @param request the {@link Request} entity to be updated
     * @return the updated {@link Request} entity
     * @author Amico Matteo
     */
public Request updateRequest(Request request);

    /**
     * Deletes a Request entity with the specified ID from the database.
     * If no entity with the given ID exists, the method does nothing.
     *
     * @param id the ID of the {@link Request} entity to be deleted
     * @author Amico Matteo
     */
public  void deleteRequest(int id);

    /**
     * Retrieves all Request entities associated with a specific user ID.
     *
     * @param userId the ID of the user whose requests are to be retrieved
     * @return a list of {@link Request} entities belonging to the specified user
     * @author Amico Matteo
     */
public List<Request> getRequestsByUserId(int userId);

    /**
     * Retrieves a Request entity with the specified ID.
     *
     * @param id the unique ID of the {@link Request} to be retrieved
     * @return the {@link Request} entity with the specified ID
     * @author Amico Matteo
     */
public Request getRequestById(int id);
}
