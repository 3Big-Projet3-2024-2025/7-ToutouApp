package be.projet3.toutouapp.services;
import be.projet3.toutouapp.models.DogCategory;
import be.projet3.toutouapp.repositories.jpa.DogCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DogCategoryService implements IDogCategoryService {
    private final DogCategoryRepository dogCategoryRepository;

    @Autowired
    public DogCategoryService(DogCategoryRepository dogCategoryRepository) {
        this.dogCategoryRepository = dogCategoryRepository;
    }

    @Override
    public List<DogCategory> getAllCategories() {
        return dogCategoryRepository.findAll();
    }
}
