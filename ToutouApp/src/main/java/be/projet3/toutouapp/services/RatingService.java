package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Rating;
import be.projet3.toutouapp.repositories.jpa.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService implements IRatingService {

    @Autowired
    private RatingRepository ratingRepository;

    // Récupérer tous les avis
    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    // Récupérer les avis par utilisateur
    @Override
    public List<Rating> getRatingByUserId(int userId) {
        return ratingRepository.findByConsumer_Id(userId);
    }

    // Ajouter un avis
    @Override
    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    // Supprimer un avis
    @Override
    public void deleteRating(int id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
        }
    }

    @Override
    public List<Rating> getNegativeRatings() {
        return ratingRepository.findByRatingValueLessThan(3); // Avis avec une note < 3
    }

}
