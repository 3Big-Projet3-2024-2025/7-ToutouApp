package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.DogCategory;
import java.util.List;

public interface IDogCategoryService {
    List<DogCategory> getAllCategories();
}
