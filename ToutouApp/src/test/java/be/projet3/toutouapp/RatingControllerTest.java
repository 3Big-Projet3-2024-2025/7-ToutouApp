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
}
