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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RatingControllerTest {

    @Mock
    private IRatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNegativeRatings_shouldReturnNegativeRatingsDTOList() {
        // Préparer les données mockées
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

        // Appeler la méthode
        ResponseEntity<List<RatingInfoDTO>> response = ratingController.getNegativeRatings();

        // Vérifier les résultats
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        RatingInfoDTO dto = response.getBody().get(0);

        assertEquals(1, dto.getId());
        assertEquals("John Doe", dto.getEvaluatedUserName());
        assertEquals("Jane Smith", dto.getRaterUserName());
        assertEquals(2, dto.getRatingValue());
        assertEquals("Bad experience", dto.getComment());
        assertEquals("2023-12-31", dto.getRequestDate());

        // Vérifier les interactions avec le mock
        verify(ratingService, times(1)).getNegativeRatings();
    }

    @Test
    void getNegativeRatings_shouldReturnEmptyList_whenNoNegativeRatings() {
        // Préparer les données mockées
        when(ratingService.getNegativeRatings()).thenReturn(List.of());

        // Appeler la méthode
        ResponseEntity<List<RatingInfoDTO>> response = ratingController.getNegativeRatings();

        // Vérifier les résultats
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());

        // Vérifier les interactions avec le mock
        verify(ratingService, times(1)).getNegativeRatings();
    }

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

    @Test
    void deleteRating_shouldCallServiceDeleteMethod() {
        doNothing().when(ratingService).deleteRating(1);

        ResponseEntity<Void> response = ratingController.deleteRating(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(ratingService, times(1)).deleteRating(1);
    }

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
}
