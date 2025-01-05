package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Rating;

import java.util.List;

/**
 * Interface for managing Rating entities.
 * Defines the contract for business logic operations related to Ratings.
 * @see be.projet3.toutouapp.services
 * @author Amico Matteo
 */
public interface IRatingService {

    /**
     * Retrieves all Rating entities from the database.
     *
     * @return a list of all {@link Rating} entities
     * @author Amico Matteo
     */
    List<Rating> getAllRatings();

    /**
     * Retrieves all Rating entities associated with a specific user ID.
     *
     * @param userId the ID of the user whose ratings are to be retrieved
     * @return a list of {@link Rating} entities belonging to the specified user
     * @author Amico Matteo
     */
    List<Rating> getRatingByUserId(int userId);

    /**
     * Adds a new Rating entity to the database.
     *
     * @param rating the {@link Rating} entity to be added
     * @return the saved {@link Rating} entity
     * @author Amico Matteo
     */
    Rating addRating(Rating rating);

    /**
     * Deletes a Rating entity with the specified ID from the database.
     * If no entity with the given ID exists, the method does nothing.
     *
     * @param id the ID of the {@link Rating} entity to be deleted
     * @author Amico Matteo
     */
    void deleteRating(int id);


    /**
     * Retrieves all Rating entities with a rating value less than 3.
     * Used to identify negative reviews.
     *
     * @return a list of {@link Rating} entities with a rating value less than 3
     * @author Sirjacques Célestin
     */
    List<Rating> getNegativeRatings();

}
