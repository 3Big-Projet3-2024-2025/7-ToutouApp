package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides methods for database operations on the User table.
 *
 * @see be.projet3.toutouapp.repositories.jpa
 * @author Jaï Dusépulchre
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieves a {@link User} entity by its email address.
     *
     * @param mail the email address of the user to retrieve.
     * @return the {@link User} entity associated with the given email address, or null if no user is found.
     */
    User findByMail(String mail);

    /**
     * Checks whether a {@link User} with the given email address exists.
     *
     * @param mail the email address to check for existence.
     * @return true if a user with the given email address exists, false otherwise.
     */
    boolean existsByMail(String mail);
}

