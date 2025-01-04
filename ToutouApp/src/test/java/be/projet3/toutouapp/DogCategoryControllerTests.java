package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.DogCategoryController;
import be.projet3.toutouapp.models.DogCategory;
import be.projet3.toutouapp.services.DogCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DogCategoryControllerTests {

    @InjectMocks
    private DogCategoryController dogCategoryController;

    @Mock
    private DogCategoryService dogCategoryService;

    private DogCategory dogCategory1;
    private DogCategory dogCategory2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dogCategory1 = new DogCategory();
        dogCategory1.setDogCategoryId(1);
        dogCategory1.setCategory("Labrador");

        dogCategory2 = new DogCategory();
        dogCategory2.setDogCategoryId(2);
        dogCategory2.setCategory("Bulldog");
    }

    @Test
    void testGetDogCategories_Success() {
        List<DogCategory> categories = new ArrayList<>();
        categories.add(dogCategory1);
        categories.add(dogCategory2);

        when(dogCategoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<DogCategory>> response = dogCategoryController.getDogCategories();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(dogCategory1));
        assertTrue(response.getBody().contains(dogCategory2));
        verify(dogCategoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetDogCategories_NoCategories() {
        List<DogCategory> categories = new ArrayList<>();
        when(dogCategoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<DogCategory>> response = dogCategoryController.getDogCategories();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(dogCategoryService, times(1)).getAllCategories();
    }
}
