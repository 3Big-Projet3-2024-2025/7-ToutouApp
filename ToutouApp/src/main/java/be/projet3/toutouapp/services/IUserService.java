package be.projet3.toutouapp.services;
import be.projet3.toutouapp.models.User;

import java.util.List;

/**
 * Interface for managing User entities.
 * Defines the contract for business logic operations related to Users.
 * @see be.projet3.toutouapp.services
 * @author Jaï Dusépulchre
 */
public interface IUserService {

    /**
     * Adds a new User entity to the database.
     *
     * @param user the {@link User} entity to be added
     * @return the saved {@link User} entity
     * @author Jaï Dusépulchre
     */
    public User addUser(User user);

    /**
     * Retrieves all User entities from the database.
     *
     * @return a list of all {@link User} entities
     * @author Jaï Dusépulchre
     */
    public List<User> getAllUsers();

    /**
     * Retrieves a User entity with the specified ID.
     *
     * @param id the unique ID of the {@link User} to be retrieved
     * @return the {@link User} entity with the specified ID, or {@code null} if not found
     * @author Jaï Dusépulchre
     */
    public User getUserById(int id);

    /**
     * Updates an existing User entity in the database.
     *
     * @param user the {@link User} entity to be updated
     * @return the updated {@link User} entity
     * @author Jaï Dusépulchre
     */
    public User updateUser(User user);

    /**
     * Deletes a User entity with the specified ID from the database.
     * If no entity with the given ID exists, the method does nothing.
     *
     * @param id the ID of the {@link User} entity to be deleted
     * @author Jaï Dusépulchre
     */
    public  void deleteUser(int id);
}