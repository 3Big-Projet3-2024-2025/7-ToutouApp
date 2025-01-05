package be.projet3.toutouapp.repositories.jpa;


import be.projet3.toutouapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @see be.projet3.toutouapp.repositories.jpa
 * Repository interface for accessing and performing CRUD operations on {@link Role} entities.
 * This interface extends {@link JpaRepository}, providing various methods to interact with the database.
 * @author Jaï Dusépulchre
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Finds a {@link Role} by its name.
     *
     * @param name the name of the role.
     * @return an {@link Optional} containing the {@link Role} if found, otherwise empty.
     */
    Optional<Role> findByName(String name);

    /**
     * Finds a {@link Role} by its ID.
     *
     * @param id the ID of the role.
     * @return an {@link Optional} containing the {@link Role} if found, otherwise empty.
     */
    Optional<Role> findById(int id);
}
