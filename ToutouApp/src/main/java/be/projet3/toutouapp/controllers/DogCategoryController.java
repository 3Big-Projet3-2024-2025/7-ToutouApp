package be.projet3.toutouapp.controllers;

import be.projet3.toutouapp.models.DogCategory;
import be.projet3.toutouapp.services.DogCategoryService;
import be.projet3.toutouapp.services.IDogCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DogCategoryController {

    private final DogCategoryService dogCategoryService;

    @Autowired
    public DogCategoryController(DogCategoryService dogCategoryService) {
        this.dogCategoryService = dogCategoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<DogCategory>> getDogCategories() {
        List<DogCategory> categories = dogCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
