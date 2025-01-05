package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @see be.projet3.toutouapp.repositories.jpa
 * Repository interface for managing Rating entities in the database.
 * Extends {@link JpaRepository} to provide CRUD operations and custom query methods.
 * @author Amico Matteo
 */
public interface RatingRepository extends JpaRepository <Rating, Integer> {

    /**
     * Retrieves a list of {@link Rating} entities associated with a specific consumer ID.
     *
     * @param consumerId the ID of the consumer whose ratings are to be retrieved
     * @return a list of {@link Rating} entities belonging to the specified consumer
     * @author Amico Matteo
     */
    List<Rating> findByConsumer_Id(int consumerId);

    /**
     * Retrieves a list of {@link Rating} entities with a rating value less than the specified value.
     *
     * @param value the threshold value for filtering ratings
     * @return a list of {@link Rating} entities with a rating value less than the specified value
     * @author Amico Matteo
     */
    List<Rating> findByRatingValueLessThan(int value);
}
