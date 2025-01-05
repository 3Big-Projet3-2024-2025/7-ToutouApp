package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @see be.projet3.toutouapp.repositories.jpa
 * Repository interface for managing Request entities in the database.
 * Extends {@link JpaRepository} to provide CRUD operations and custom query methods.
 * This interface is annotated with {@link Repository} to indicate it's a Spring repository.
 * @author Amico Matteo
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    /**
     * Retrieves a list of {@link Request} entities associated with a specific owner ID.
     *
     * @param ownerId the ID of the owner whose requests are to be retrieved
     * @return a list of {@link Request} entities belonging to the specified owner
     * @author Amico Matteo
     */
    List<Request> findByOwner_Id(int ownerId);

    /**
     * Retrieves a single {@link Request} entity based on its unique request ID.
     *
     * @param requestId the unique ID of the request to be retrieved
     * @return the {@link Request} entity with the specified request ID, or {@code null} if none found
     * @author Amico Matteo
     */
    Request findByRequestId(int requestId);

}
