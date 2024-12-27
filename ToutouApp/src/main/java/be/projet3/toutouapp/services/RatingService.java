package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Rating;
import be.projet3.toutouapp.repositories.jpa.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService implements IRatingService{

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public List<Rating> getRatingByUserId(int userId) {
        return ratingRepository.findByConsumer_Id(userId);
    }

    @Override
    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public void deleteRating(int id) {
        if (ratingRepository.existsById(id)){
            ratingRepository.deleteById(id);
        }

    }
}
