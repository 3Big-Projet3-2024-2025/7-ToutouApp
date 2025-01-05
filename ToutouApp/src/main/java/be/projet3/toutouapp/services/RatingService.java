package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Rating;
import be.projet3.toutouapp.repositories.jpa.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Rating entities.
 * Implements the {@link IRatingService} interface to provide business logic for Rating operations.
 * Annotated with {@link Service} to indicate it's a Spring service component.
 * @see be.projet3.toutouapp.services
 * @author Amico Matteo
 */

@Service
public class RatingService implements IRatingService {


    /**
     *
     * Repository for performing CRUD operations on Rating entities.
     * Automatically injected by Spring using the {@link Autowired} annotation.
     */
    @Autowired
    private RatingRepository ratingRepository;


    /**
     * Service for sending email notifications.
     * Automatically injected by Spring using the {@link Autowired} annotation.
     */
    @Autowired
    private EmailService emailService;


    /**
     * Email address of the administrator to notify in case of negative reviews.
     */
    private static final String ADMIN_EMAIL = "celestinsirjacques@gmail.com";

    /**
     * Retrieves all Rating entities from the database.
     *
     * @return a list of all {@link Rating} entitiesµ
     * @author Amico Matteo
     */
    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    /**
     * Retrieves all Rating entities associated with a specific user ID.
     *
     * @param userId the ID of the user whose ratings are to be retrieved
     * @return a list of {@link Rating} entities belonging to the specified user
     * @author Amico Matteo
     */
    @Override
    public List<Rating> getRatingByUserId(int userId) {
        return ratingRepository.findByConsumer_Id(userId);
    }

    /**
     * Adds a new Rating entity to the database.
     * Sends an email notification to the administrator if the rating value is less than 3.
     *
     * @param rating the {@link Rating} entity to be added
     * @return the saved {@link Rating} entity
     * @author Sirjacques Célestin
     */
    @Override
    public Rating addRating(Rating rating) {
        Rating newRating = ratingRepository.save(rating);


        if (newRating.getRatingValue() < 3) {
            String subject = "Notification: New Negative Review";
            String body = "A review with a rating of " + newRating.getRatingValue() + " has been submitted.\n\n" +
                    "Please check it.";

            emailService.sendEmail(ADMIN_EMAIL, subject, body);
        }

        return newRating;
    }

    /**
     * Deletes a Rating entity with the specified ID from the database.
     * If no entity with the given ID exists, the method does nothing.
     *
     * @param id the ID of the {@link Rating} entity to be deleted
     * @author Amico Matteo
     */
    @Override
    public void deleteRating(int id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
        }
    }

    /**
     * Retrieves all Rating entities with a rating value less than 3.
     * Used to identify negative reviews.
     *
     * @return a list of {@link Rating} entities with a rating value less than 3
     * @author Sirjacques Célestin
     */
    @Override
    public List<Rating> getNegativeRatings() {
        return ratingRepository.findByRatingValueLessThan(3); // Avis avec une note < 3
    }
}
