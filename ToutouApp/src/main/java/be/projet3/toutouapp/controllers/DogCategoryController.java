package be.projet3.toutouapp.controllers;

import be.projet3.toutouapp.models.DogCategory;
import be.projet3.toutouapp.services.DogCategoryService;
import be.projet3.toutouapp.services.IDogCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for managing dog categories.
 * Provides an endpoint to retrieve all dog categories.
 *
 * @see be.projet3.toutouapp.controllers
 * @author Jaï Dusépulchre
 */
@RestController
@RequestMapping("/api")
public class DogCategoryController {

    @Autowired
    private DogCategoryService dogCategoryService;

    /**
     * Retrieves all dog categories.
     *
     * @return ResponseEntity containing a list of DogCategory objects and HTTP status:
     * - 200 (OK) if the categories are successfully retrieved
     * - 204 (No Content) if no categories are available
     * - 500 (Internal Server Error) if an unexpected error occurs
     */
    @GetMapping("/categories")
    public ResponseEntity<List<DogCategory>> getDogCategories() {
        try {
            List<DogCategory> categories = dogCategoryService.getAllCategories();

            if (categories.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return HTTP 204 No Content
            }

            // Return categories with HTTP 200 OK
            return ResponseEntity.ok(categories);

        } catch (Exception e) {
            // Return HTTP 500 Internal Server Error in case of failure
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
