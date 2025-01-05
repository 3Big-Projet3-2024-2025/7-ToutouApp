package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.RatingController;
import be.projet3.toutouapp.dto.RatingInfoDTO;
import be.projet3.toutouapp.models.Rating;
import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.services.IRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test class for RatingController.
 * Author: Amico Matteo, Sirjacques Célestin
 */
class RatingControllerTest {

    @Mock
    private IRatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /**
     * Test retrieving negative ratings and returning a list of RatingInfoDTO.
     * Verifies that the response contains the correct details for negative ratings.
     */
    @Test
    void getNegativeRatings_shouldReturnNegativeRatingsDTOList() {
        // Prepare mocked data
        User consumer = new User();
        consumer.setId(1);
        consumer.setFirstName("John");
        consumer.setLastName("Doe");

        User owner = new User();
        owner.setId(2);
        owner.setFirstName("Jane");
        owner.setLastName("Smith");

        Request request = new Request();
        request.setRequestId(100);
        request.setRequestDate(LocalDate.of(2023, 12, 31));
        request.setOwner(owner);
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));

        Rating rating = new Rating();
        rating.setRatingId(1);
        rating.setRatingValue(2);
        rating.setComment("Bad experience");
        rating.setConsumer(consumer);
        rating.setRequest(request);

        List<Rating> negativeRatings = Arrays.asList(rating);
        when(ratingService.getNegativeRatings()).thenReturn(negativeRatings);

        // Call the method
        ResponseEntity<List<RatingInfoDTO>> response = ratingController.getNegativeRatings();

        // Verify the results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        RatingInfoDTO dto = response.getBody().get(0);

        assertEquals(1, dto.getId());
        assertEquals("John Doe", dto.getEvaluatedUserName());
        assertEquals("Jane Smith", dto.getRaterUserName());
        assertEquals(2, dto.getRatingValue());
        assertEquals("Bad experience", dto.getComment());
        assertEquals("2023-12-31", dto.getRequestDate());

        // Verify interactions with the mock
        verify(ratingService, times(1)).getNegativeRatings();
    }

    /**
     * Test retrieving negative ratings when no negative ratings are available.
     * Verifies that the response contains an empty list.
     */
    @Test
    void getNegativeRatings_shouldReturnEmptyList_whenNoNegativeRatings() {
        // Prepare mocked data
        when(ratingService.getNegativeRatings()).thenReturn(List.of());

        // Call the method
        ResponseEntity<List<RatingInfoDTO>> response = ratingController.getNegativeRatings();

        // Verify the results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());

        // Verify interactions with the mock
        verify(ratingService, times(1)).getNegativeRatings();
    }


    /**
     * Test retrieving multiple negative ratings and returning the correct details.
     * Verifies that multiple negative ratings are correctly handled and returned.
     */
    @Test
    void getNegativeRatings_shouldHandleMultipleNegativeRatings() {
        // Prepare mocked data
        User consumer1 = new User();
        consumer1.setId(1);
        consumer1.setFirstName("John");
        consumer1.setLastName("Doe");

        User consumer2 = new User();
        consumer2.setId(2);
        consumer2.setFirstName("Alice");
        consumer2.setLastName("Johnson");

        User owner = new User();
        owner.setId(3);
        owner.setFirstName("Jane");
        owner.setLastName("Smith");

        Request request1 = new Request();
        request1.setRequestId(100);
        request1.setRequestDate(LocalDate.of(2023, 12, 31));
        request1.setOwner(owner);
        request1.setStartTime(LocalTime.of(10, 0));
        request1.setEndTime(LocalTime.of(12, 0));

        Request request2 = new Request();
        request2.setRequestId(101);
        request2.setRequestDate(LocalDate.of(2023, 11, 30));
        request2.setOwner(owner);
        request2.setStartTime(LocalTime.of(14, 0));
        request2.setEndTime(LocalTime.of(16, 0));

        Rating rating1 = new Rating();
        rating1.setRatingId(1);
        rating1.setRatingValue(2);
        rating1.setComment("Bad experience");
        rating1.setConsumer(consumer1);
        rating1.setRequest(request1);

        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        rating2.setRatingValue(1);
        rating2.setComment("Terrible service");
        rating2.setConsumer(consumer2);
        rating2.setRequest(request2);

        List<Rating> negativeRatings = Arrays.asList(rating1, rating2);
        when(ratingService.getNegativeRatings()).thenReturn(negativeRatings);

        // Call the method
        ResponseEntity<List<RatingInfoDTO>> response = ratingController.getNegativeRatings();

        // Verify the results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

        RatingInfoDTO dto1 = response.getBody().get(0);
        assertEquals(1, dto1.getId());
        assertEquals("John Doe", dto1.getEvaluatedUserName());
        assertEquals("Jane Smith", dto1.getRaterUserName());
        assertEquals(2, dto1.getRatingValue());
        assertEquals("Bad experience", dto1.getComment());
        assertEquals("2023-12-31", dto1.getRequestDate());

        RatingInfoDTO dto2 = response.getBody().get(1);
        assertEquals(2, dto2.getId());
        assertEquals("Alice Johnson", dto2.getEvaluatedUserName());
        assertEquals("Jane Smith", dto2.getRaterUserName());
        assertEquals(1, dto2.getRatingValue());
        assertEquals("Terrible service", dto2.getComment());
        assertEquals("2023-11-30", dto2.getRequestDate());

        // Verify interactions with the mock
        verify(ratingService, times(1)).getNegativeRatings();
    }


    /**
     * Test retrieving negative ratings when the service fails.
     * Verifies that the exception is correctly thrown and handled.
     */
    @Test
    void getNegativeRatings_shouldThrowException_whenServiceFails() {
        // Simulate an exception in the service
        when(ratingService.getNegativeRatings()).thenThrow(new RuntimeException("Database error"));

        // Call the method and verify the results
        try {
            ratingController.getNegativeRatings();
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Database error", e.getMessage());
        }

        // Verify interactions with the mock
        verify(ratingService, times(1)).getNegativeRatings();
    }


    /**
     * Test retrieving all ratings.
     * Verifies that the correct list of ratings is returned with the correct details.
     */
    @Test

    void getAllRatings_shouldReturnListOfRatings() {
        User consumer = new User();
        consumer.setId(1);

        Request request = new Request();
        request.setRequestId(100);

        Rating rating1 = new Rating();
        rating1.setRatingId(1);
        rating1.setRatingValue(5);
        rating1.setComment("Excellent service");
        rating1.setConsumer(consumer);
        rating1.setRequest(request);

        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        rating2.setRatingValue(4);
        rating2.setComment("Good service");
        rating2.setConsumer(consumer);
        rating2.setRequest(request);

        List<Rating> ratings = Arrays.asList(rating1, rating2);
        when(ratingService.getAllRatings()).thenReturn(ratings);

        ResponseEntity<List<Rating>> response = ratingController.getAllRatings();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Excellent service", response.getBody().get(0).getComment());
        assertEquals("Good service", response.getBody().get(1).getComment());

        verify(ratingService, times(1)).getAllRatings();
    }


    /**
     * Test adding a new rating.
     * Verifies that a new rating is created successfully and returned.
     */
    @Test
    void addRating_shouldCreateNewRating() {
        User consumer = new User();
        consumer.setId(1);

        Request request = new Request();
        request.setRequestId(100);

        Rating newRating = new Rating();
        newRating.setRatingValue(5);
        newRating.setComment("Great experience");
        newRating.setConsumer(consumer);
        newRating.setRequest(request);

        Rating savedRating = new Rating();
        savedRating.setRatingId(1);
        savedRating.setRatingValue(5);
        savedRating.setComment("Great experience");
        savedRating.setConsumer(consumer);
        savedRating.setRequest(request);

        when(ratingService.addRating(any(Rating.class))).thenReturn(savedRating);

        ResponseEntity<Rating> response = ratingController.addRating(newRating);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getRatingId());
        assertEquals("Great experience", response.getBody().getComment());

        verify(ratingService, times(1)).addRating(any(Rating.class));
    }


    /**
     * Test deleting a rating.
     * Verifies that the delete method of the service is correctly called.
     */
    @Test
    void deleteRating_shouldCallServiceDeleteMethod() {
        doNothing().when(ratingService).deleteRating(1);

        ResponseEntity<Void> response = ratingController.deleteRating(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(ratingService, times(1)).deleteRating(1);
    }


    /**
     * Test retrieving ratings by user ID.
     * Verifies that the list of ratings for a user is returned correctly.
     */
    @Test
    void getRatingByUserId_shouldReturnListOfRatingsForUser() {
        User consumer = new User();
        consumer.setId(1);

        Request request = new Request();
        request.setRequestId(100);

        Rating rating1 = new Rating();
        rating1.setRatingId(1);
        rating1.setRatingValue(4);
        rating1.setComment("Good service");
        rating1.setConsumer(consumer);
        rating1.setRequest(request);

        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        rating2.setRatingValue(3);
        rating2.setComment("Satisfactory");
        rating2.setConsumer(consumer);
        rating2.setRequest(request);

        List<Rating> ratings = Arrays.asList(rating1, rating2);
        when(ratingService.getRatingByUserId(1)).thenReturn(ratings);

        List<Rating> response = ratingController.getRatingBuUserId(1);

        assertEquals(2, response.size());
        assertEquals("Good service", response.get(0).getComment());
        assertEquals("Satisfactory", response.get(1).getComment());

        verify(ratingService, times(1)).getRatingByUserId(1);
    }

    /**
     * Test retrieving negative ratings when no positive ratings exist.
     * Verifies that positive ratings should not be included.
     */

    @Test
    void getNegativeRatings_shouldNotIncludePositiveRatings() {
        // Prepare mocked data
        User consumer = new User();
        consumer.setId(1);
        consumer.setFirstName("John");
        consumer.setLastName("Doe");

        User owner = new User();
        owner.setId(2);
        owner.setFirstName("Jane");
        owner.setLastName("Smith");

        Request request = new Request();
        request.setRequestId(100);
        request.setRequestDate(LocalDate.of(2023, 12, 31));
        request.setOwner(owner);
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));

        Rating positiveRating = new Rating();
        positiveRating.setRatingId(1);
        positiveRating.setRatingValue(5);
        positiveRating.setComment("Great experience");
        positiveRating.setConsumer(consumer);
        positiveRating.setRequest(request);

        List<Rating> ratings = Arrays.asList(positiveRating);
        when(ratingService.getNegativeRatings()).thenReturn(ratings);

        // Call the method
        ResponseEntity<List<RatingInfoDTO>> response = ratingController.getNegativeRatings();

        // Verify the results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size()); // One rating is included because the controller does not filter

        RatingInfoDTO dto = response.getBody().get(0);
        assertEquals(1, dto.getId());
        assertEquals("John Doe", dto.getEvaluatedUserName());
        assertEquals("Jane Smith", dto.getRaterUserName());
        assertEquals(5, dto.getRatingValue());
        assertEquals("Great experience", dto.getComment());
        assertEquals("2023-12-31", dto.getRequestDate());

        // Verify interactions with the mock
        verify(ratingService, times(1)).getNegativeRatings();
    }


    /**
     * Test for handling large datasets of negative ratings.
     * Verifies that the method can handle a large number of negative ratings (1000 in this case) and returns them correctly.
     */
    @Test
    void getNegativeRatings_shouldHandleLargeDataset() {
        // Prepare a large list of mocked data
        List<Rating> negativeRatings = IntStream.range(0, 1000)
                .mapToObj(i -> {
                    User consumer = new User();
                    consumer.setId(i);
                    consumer.setFirstName("User" + i);
                    consumer.setLastName("Lastname" + i);

                    User owner = new User();
                    owner.setId(i + 1000); // Unique ID for the owner
                    owner.setFirstName("Owner" + i);
                    owner.setLastName("OwnerLastname" + i);

                    Request request = new Request();
                    request.setRequestId(i);
                    request.setRequestDate(LocalDate.now());
                    request.setOwner(owner); // Set the owner

                    Rating rating = new Rating();
                    rating.setRatingId(i);
                    rating.setRatingValue(2);
                    rating.setComment("Negative comment " + i);
                    rating.setConsumer(consumer);
                    rating.setRequest(request);

                    return rating;
                }).collect(Collectors.toList());

        when(ratingService.getNegativeRatings()).thenReturn(negativeRatings);

        // Call the method
        ResponseEntity<List<RatingInfoDTO>> response = ratingController.getNegativeRatings();

        // Verify the results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1000, response.getBody().size());

        // Verify interactions with the mock
        verify(ratingService, times(1)).getNegativeRatings();
    }


    /**
     * Test for getting all ratings when there are no ratings available.
     * Verifies that the response returns an empty list when no ratings exist.
     */
    @Test
    void getAllRatings_shouldReturnEmptyList_whenNoRatings() {
        // Prepare mocked data
        when(ratingService.getAllRatings()).thenReturn(List.of());

        // Call the method
        ResponseEntity<List<Rating>> response = ratingController.getAllRatings();

        // Verify the results
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());

        // Verify interactions with the mock
        verify(ratingService, times(1)).getAllRatings();
    }

    /**
     * Test for deleting a rating when the rating does not exist.
     * Verifies that the method correctly returns a 404 status when trying to delete a non-existing rating.
     */
    @Test
    void deleteRating_shouldReturnNotFound_whenRatingDoesNotExist() {
        // Simulate the service returning no such rating
        doThrow(new RuntimeException("Rating not found")).when(ratingService).deleteRating(999);

        // Call the method and verify the results
        try {
            ResponseEntity<Void> response = ratingController.deleteRating(999);
            assertEquals(404, response.getStatusCodeValue());
        } catch (RuntimeException e) {
            assertEquals("Rating not found", e.getMessage());
        }

        // Verify interactions with the mock
        verify(ratingService, times(1)).deleteRating(999);
    }



    /**
     * Test for getting ratings by user ID when the user has no ratings.
     * Verifies that the response correctly returns an empty list when no ratings exist for the user.
     */
    @Test
    void getRatingByUserId_shouldReturnEmptyList_whenNoRatingsForUser() {
        // Prepare mocked data
        when(ratingService.getRatingByUserId(1)).thenReturn(List.of());

        // Call the method
        List<Rating> response = ratingController.getRatingBuUserId(1);

        // Verify the results
        assertEquals(0, response.size());

        // Verify interactions with the mock
        verify(ratingService, times(1)).getRatingByUserId(1);
    }



    /**
     * Test for getting all ratings for a user when the user has multiple ratings.
     * Verifies that all ratings for the user are returned correctly.
     */
    @Test
    void getRatingByUserId_shouldReturnAllRatingsForUser() {
        // Prepare mocked data for multiple ratings
        User consumer = new User();
        consumer.setId(2);
        consumer.setFirstName("Jake");
        consumer.setLastName("Peralta");

        Request request = new Request();
        request.setRequestId(101);

        Rating rating1 = new Rating();
        rating1.setRatingId(1);
        rating1.setRatingValue(5);
        rating1.setComment("Excellent service");
        rating1.setConsumer(consumer);
        rating1.setRequest(request);

        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        rating2.setRatingValue(4);
        rating2.setComment("Good service");
        rating2.setConsumer(consumer);
        rating2.setRequest(request);

        List<Rating> ratings = Arrays.asList(rating1, rating2);
        when(ratingService.getRatingByUserId(2)).thenReturn(ratings);

        // Call the method
        List<Rating> response = ratingController.getRatingBuUserId(2);

        // Verify the results
        assertEquals(2, response.size());
        assertEquals("Excellent service", response.get(0).getComment());
        assertEquals("Good service", response.get(1).getComment());

        // Verify interactions with the mock
        verify(ratingService, times(1)).getRatingByUserId(2);
    }


    /**
     * Test for getting ratings by user ID when the user does not exist.
     * Verifies that the response returns an empty list when no ratings exist for the given user ID.
     */
    @Test
    void getRatingByUserId_shouldReturnEmptyList_whenUserDoesNotExist() {
        // Prepare mocked data
        when(ratingService.getRatingByUserId(999)).thenReturn(List.of());

        // Call the method
        List<Rating> response = ratingController.getRatingBuUserId(999);

        // Verify the results
        assertEquals(0, response.size());

        // Verify interactions with the mock
        verify(ratingService, times(1)).getRatingByUserId(999);
    }

}
