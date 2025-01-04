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

    @Autowired
    private EmailService emailService; // Injection du service d'envoi d'e-mails

    private static final String ADMIN_EMAIL = "celestinsirjacques@gmail.com";

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

    // Add a review
    @Override
    public Rating addRating(Rating rating) {
        Rating newRating = ratingRepository.save(rating);

        // Check if the rating value is less than 3 before sending an email
        if (newRating.getRatingValue() < 3) {
            String subject = "Notification: New Negative Review";
            String body = "A review with a rating of " + newRating.getRatingValue() + " has been submitted.\n\n" +
                    "Please check it.";

            emailService.sendEmail(ADMIN_EMAIL, subject, body);
        }

        return newRating;
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
