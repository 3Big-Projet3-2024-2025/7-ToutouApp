package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.DogCategoryController;
import be.projet3.toutouapp.models.DogCategory;
import be.projet3.toutouapp.services.DogCategoryService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * These tests validate the retrieval of dog categories from the API.
 *
 * @see be.projet3.toutouapp
 * @author Jaï Dusépulchre
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DogCategoryControllerTests {

    @InjectMocks
    private DogCategoryController dogCategoryController;

    @Mock
    private DogCategoryService dogCategoryService;

    private DogCategory dogCategory1;
    private DogCategory dogCategory2;

    /**
     * Initializes mocks and sets up the test data before each test.
     */
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

    /**
     * Tests the successful retrieval of dog categories when there are categories available.
     * This test ensures that the API returns the expected categories and status code 200.
     */
    @Test
    @Order(1)
    @DisplayName("Test successful retrieval of dog categories")
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

    /**
     * Tests the case when no dog categories are available.
     * This test ensures that the API returns HTTP 204 No Content when no categories exist.
     */
    @Test
    @Order(2)
    @DisplayName("Test retrieval of dog categories when none are available (204 No Content)")
    void testGetDogCategories_NoCategories() {
        // Prepare the mock behavior for an empty list
        List<DogCategory> categories = new ArrayList<>();
        when(dogCategoryService.getAllCategories()).thenReturn(categories);

        // Call the controller method
        ResponseEntity<List<DogCategory>> response = dogCategoryController.getDogCategories();

        // Assertions
        assertEquals(204, response.getStatusCodeValue());  // Expect HTTP 204 No Content
        assertNull(response.getBody());  // Body should be null for 204 No Content response
        assertTrue(response.getBody() == null || response.getBody().isEmpty());

        // Verify the service method is called
        verify(dogCategoryService, times(1)).getAllCategories();
    }


    /**
     * Tests the scenario when an internal server error occurs during the retrieval of dog categories.
     * This test simulates an exception being thrown in the service layer.
     */
    @Test
    @Order(3)
    @DisplayName("Test handling of internal server error during dog categories retrieval")
    void testGetDogCategories_InternalServerError() {
        // Prepare the mock behavior to throw an exception
        when(dogCategoryService.getAllCategories()).thenThrow(new RuntimeException("Internal server error"));

        // Call the controller method
        ResponseEntity<List<DogCategory>> response = dogCategoryController.getDogCategories();

        // Assertions
        assertEquals(500, response.getStatusCodeValue());  // Expect HTTP 500 Internal Server Error
        assertNull(response.getBody());

        // Verify the service method is called
        verify(dogCategoryService, times(1)).getAllCategories();
    }
}
