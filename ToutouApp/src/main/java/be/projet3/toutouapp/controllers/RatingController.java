package be.projet3.toutouapp.controllers;

import be.projet3.toutouapp.dto.RatingInfoDTO;
import be.projet3.toutouapp.models.Rating;
import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.services.IRatingService;
import be.projet3.toutouapp.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller for managing ratings in the application.
 * This class provides the endpoints for interacting with ratings, including retrieving, creating, and deleting ratings,
 * as well as retrieving negative ratings with detailed information.
 *
 * @author Amico Matteo
 */
@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private IRatingService ratingService;


    /**
     * Retrieves all ratings.
     * This endpoint returns a list of all ratings with HTTP status 200 OK.
     *
     * @return A ResponseEntity containing the list of ratings and HTTP status 200.
     * @author Amico Matteo
     */
    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingService.getAllRatings();
        return ResponseEntity.ok(ratings); // HTTP 200 OK avec la liste des avis
    }

    /**
     * Retrieves all ratings for a specific user.
     * This endpoint returns a list of ratings associated with the specified user ID.
     *
     * @param userId The ID of the user whose ratings are to be retrieved.
     * @return A list of ratings for the specified user.
     * @author Amico Matteo
     */
    @GetMapping("/user/{userId}")
        public List<Rating> getRatingBuUserId(@PathVariable int userId){
        return ratingService.getRatingByUserId(userId);
    }


    /**
     * Deletes a rating by its ID.
     * This endpoint deletes the rating identified by the provided ID and returns HTTP 204 No Content status.
     *
     * @param id The ID of the rating to be deleted.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     * @author Amico Matteo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable int id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    /**
     * Adds a new rating.
     * This endpoint creates a new rating and returns the created rating with HTTP status 201 Created.
     *
     * @param rating The rating to be added.
     * @return A ResponseEntity containing the newly created rating and HTTP status 201.
     * @author Amico Matteo
     */
    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody  Rating rating){
        Rating newRating = ratingService.addRating(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRating);
    }


    /**
     * Retrieves all negative ratings with detailed information.
     * This endpoint returns only negative ratings, with additional details like evaluated user, rater user, and request information.
     *
     * @return A ResponseEntity containing the list of negative ratings with detailed information.
     * @author Sirjacques Célestin
     */
    @GetMapping("/negative")
    public ResponseEntity<List<RatingInfoDTO>> getNegativeRatings() {
        List<Rating> negativeRatings = ratingService.getNegativeRatings();

        List<RatingInfoDTO> negativeRatingsDTO = negativeRatings.stream().map(rating -> {
            RatingInfoDTO dto = new RatingInfoDTO();

            // Add ID
            dto.setId(rating.getRatingId());

            // Evaluated person
            String evaluatedUserName = rating.getConsumer().getFirstName() + " " + rating.getConsumer().getLastName();
            dto.setEvaluatedUserName(evaluatedUserName);

            // Person giving the review
            String raterUserName;
            Request request = rating.getRequest();
            if (request.getOwner().getId() == rating.getConsumer().getId()) {
                raterUserName = request.getHelper() != null
                        ? request.getHelper().getFirstName() + " " + request.getHelper().getLastName()
                        : "Unknown Helper";
            } else {
                raterUserName = request.getOwner().getFirstName() + " " + request.getOwner().getLastName();
            }
            dto.setRaterUserName(raterUserName);

            // Add other fields
            dto.setRatingValue(rating.getRatingValue());
            dto.setComment(rating.getComment());

            // Add the request date
            dto.setRequestDate(request.getRequestDate().toString());

            return dto;
        }).toList();

        return ResponseEntity.ok(negativeRatingsDTO); // Returns only negative ratings
    }

}
