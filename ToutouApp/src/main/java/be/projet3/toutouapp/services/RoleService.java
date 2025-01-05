package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Role;
import be.projet3.toutouapp.repositories.jpa.RoleRepository;
import ch.qos.logback.classic.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing Role entities.
 * Provides functionality to fetch Role details from the database.
 * Annotated with {@link Service} to indicate it's a Spring service component.
 * @see be.projet3.toutouapp.services
 * @author Jaï Dusépulchre
 */
@Service
public class RoleService {

    /**
     * Repository for performing CRUD operations on Role entities.
     * Automatically injected by Spring using the {@link Autowired} annotation.
     */
    @Autowired
    private RoleRepository roleRepository;


    /**
     * Retrieves a Role entity by its unique ID.
     * Logs the process of fetching the Role and handles the case where the Role is not found.
     *
     * @param roleId the unique ID of the {@link Role} to be retrieved
     * @return the {@link Role} entity if found
     * @throws RuntimeException if no Role entity with the specified ID exists
     * @author Jaï Dusépulchre
     */
    public Role getRoleById(int roleId) {
        Logger log = null;
        log.debug("Fetching role with ID: {}", roleId);
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent()) {
            log.debug("Role found: {}", role.get());
            return role.get();
        } else {
            log.error("Role with ID {} not found", roleId);
            throw new RuntimeException("Role not found");
        }
    }
}