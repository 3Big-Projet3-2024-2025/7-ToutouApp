package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository <Rating, Integer> {
    List<Rating> findByConsumer_Id(int consumerId);
}
