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

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private IRatingService ratingService;

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingService.getAllRatings();
        return ResponseEntity.ok(ratings); // HTTP 200 OK avec la liste des avis
    }


    @GetMapping("/user/{userId}")
        public List<Rating> getRatingBuUserId(@PathVariable int userId){
        return ratingService.getRatingByUserId(userId);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable int id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody  Rating rating){
        Rating newRating = ratingService.addRating(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRating);
    }

    @GetMapping("/negative")
    public ResponseEntity<List<RatingInfoDTO>> getNegativeRatings() {
        List<Rating> negativeRatings = ratingService.getNegativeRatings();

        List<RatingInfoDTO> negativeRatingsDTO = negativeRatings.stream().map(rating -> {
            RatingInfoDTO dto = new RatingInfoDTO();

            // Ajouter l'ID
            dto.setId(rating.getRatingId()); // Assurez-vous que Rating a un champ `ratingId`

            // Personne évaluée
            String evaluatedUserName = rating.getConsumer().getFirstName() + " " + rating.getConsumer().getLastName();
            dto.setEvaluatedUserName(evaluatedUserName);

            // Personne qui donne l'avis
            String raterUserName;
            Request request = rating.getRequest();
            if (request.getOwner().getId() == rating.getConsumer().getId()) {
                raterUserName = request.getHelper() != null
                        ? request.getHelper().getFirstName() + " " + request.getHelper().getLastName()
                        : "Helper inconnu";
            } else {
                raterUserName = request.getOwner().getFirstName() + " " + request.getOwner().getLastName();
            }
            dto.setRaterUserName(raterUserName);

            // Ajouter les autres champs
            dto.setRatingValue(rating.getRatingValue());
            dto.setComment(rating.getComment());

            // Ajouter la date de la requête
            dto.setRequestDate(request.getRequestDate().toString());

            return dto;
        }).toList();

        return ResponseEntity.ok(negativeRatingsDTO); // Retourne uniquement les avis négatifs
    }


}
