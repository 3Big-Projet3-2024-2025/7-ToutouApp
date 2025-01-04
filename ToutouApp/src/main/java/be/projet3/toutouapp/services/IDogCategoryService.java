package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.DogCategory;
import java.util.List;


/**
 * Interface for managing DogCategory entities.
 * Defines the contract for business logic operations related to Dog Categories.
 * @author Jaï Dusépulchre
 */
public interface IDogCategoryService {

    /**
     * Retrieves all DogCategory entities from the database.
     *
     * @return a list of all {@link DogCategory} entities
     * @author Jaï Dusépulchre
     */
    List<DogCategory> getAllCategories();
}
