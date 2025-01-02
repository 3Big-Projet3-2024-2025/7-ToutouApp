package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Rating;

import java.util.List;

public interface IRatingService {
    // Récupérer tous les avis
    List<Rating> getAllRatings();

    // Récupérer les avis par utilisateur
    List<Rating> getRatingByUserId(int userId);

    // Ajouter un avis
    Rating addRating(Rating rating);

    // Supprimer un avis par ID
    void deleteRating(int id);

    List<Rating> getNegativeRatings();

}
