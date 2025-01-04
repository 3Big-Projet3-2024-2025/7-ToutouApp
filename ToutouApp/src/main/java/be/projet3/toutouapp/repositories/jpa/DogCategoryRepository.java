package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.DogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides methods for database operations on the DogCategory table.
 *
 * @see be.projet3.toutouapp.repositories.jpa
 * @author Jaï Dusépulchre
 */
@Repository
public interface DogCategoryRepository extends JpaRepository<DogCategory, Integer> {
}
