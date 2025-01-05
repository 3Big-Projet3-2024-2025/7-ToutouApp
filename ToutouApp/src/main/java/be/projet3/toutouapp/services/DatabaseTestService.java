package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for testing the database connection.
 * Provides a method to verify the connection by attempting to retrieve a user from the database.
 * Annotated with {@link Service} to indicate it's a Spring service component.
 * @see be.projet3.toutouapp.services
 * @author Damien DeLeeuw
 */
@Service
public class DatabaseTestService {

    /**
     * Repository for performing CRUD operations on User entities.
     * Automatically injected by Spring using the {@link Autowired} annotation.
     */
    @Autowired
    private UserRepository userRepo;


    /**
     * Tests the database connection by attempting to retrieve a User entity with a fixed ID.
     * Returns a message indicating the result of the connection test,
     * whether a user was found or if there was an issue with the database connection.
     *
     * @return a message indicating the status of the database connection and the retrieval attempt
     * @author Damien DeLeeuw
     */
    public String testDatabaseConnection() {
        try {
            int id = 1 ;
            // Essayer de récupérer un utilisateur depuis la base de données
            User user = userRepo.findById(id).orElse(null);
            if (user != null) {
                return "Connection successful, found user: " + user.getMail()+" "  + user.getRole().getName();
            } else {
                return "Connection successful, no user found.";
            }
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
}
