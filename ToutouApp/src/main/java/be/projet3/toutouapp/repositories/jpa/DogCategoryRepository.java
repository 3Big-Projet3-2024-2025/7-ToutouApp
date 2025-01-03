package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.DogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogCategoryRepository extends JpaRepository<DogCategory, Integer> {
}
