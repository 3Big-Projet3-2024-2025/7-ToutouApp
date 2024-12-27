package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Rating;

import java.util.List;

public interface IRatingService {
    public List<Rating> getRatingByUserId(int userId);
    public Rating addRating(Rating rating);
    public void deleteRating(int id);

}
