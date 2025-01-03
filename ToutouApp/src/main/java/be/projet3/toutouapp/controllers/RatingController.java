package be.projet3.toutouapp.controllers;

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
    public ResponseEntity<List<Rating>> getNegativeRatings() {
        List<Rating> negativeRatings = ratingService.getNegativeRatings();
        return ResponseEntity.ok(negativeRatings); // HTTP 200 OK
    }



}
