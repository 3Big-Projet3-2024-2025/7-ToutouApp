package be.projet3.toutouapp.services;
import be.projet3.toutouapp.models.DogCategory;
import be.projet3.toutouapp.repositories.jpa.DogCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service class for managing {@link DogCategory} entities.
 * Provides methods to interact with the {@link DogCategoryRepository}.
 *
 *@see be.projet3.toutouapp.services
 *@author Jaï Dusépulchre
 */
@Service
public class DogCategoryService implements IDogCategoryService {
    private final DogCategoryRepository dogCategoryRepository;

    /**
     * Constructs a {@link DogCategoryService} with the given {@link DogCategoryRepository}.
     *
     * @param dogCategoryRepository the repository for managing dog categories.
     */
    @Autowired
    public DogCategoryService(DogCategoryRepository dogCategoryRepository) {
        this.dogCategoryRepository = dogCategoryRepository;
    }

    /**
     * Retrieves all dog categories from the repository.
     *
     * @return a list of all {@link DogCategory} entities.
     */
    @Override
    public List<DogCategory> getAllCategories() {
        return dogCategoryRepository.findAll();
    }
}
