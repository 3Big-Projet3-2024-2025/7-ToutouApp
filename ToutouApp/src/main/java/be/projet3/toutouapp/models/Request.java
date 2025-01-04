package be.projet3.toutouapp.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a request entity in the application.
 * This entity stores details about a request for dog sitting or walking services,
 * including scheduling, the dog's details, and the state of the request.
 *
 * @see be.projet3.toutouapp.models
 * @author Damien De Leeuw
 */
@Entity
@Data
public class Request {

    /**
     * Unique identifier for the request.
     * Automatically generated using an identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    /**
     * The date when the request is scheduled.
     * This field is mandatory.
     */
    @Column(nullable = false)
    private LocalDate requestDate;

    /**
     * The time when the requested service is expected to start.
     * This field is mandatory.
     */
    @Column(nullable = false)
    private LocalTime startTime;

    /**
     * The time when the requested service is expected to end.
     * This field is mandatory.
     */
    @Column(nullable = false)
    private LocalTime endTime;

    /**
     * The name of the dog associated with the request.
     * This field is mandatory and has a maximum length of 50 characters.
     */
    @Column(nullable = false, length = 50)
    private String dogName;

    /**
     * An optional field storing the URL or path to a photo of the dog.
     * This field has a maximum length of 300 characters.
     */
    @Column(length = 300)
    private String photo;

    /**
     * An optional comment providing additional details or instructions for the request.
     * This field has a maximum length of 500 characters.
     */
    @Column(length = 500)
    private String comment;

    /**
     * The owner of the dog making the request.
     * This establishes a required many-to-one relationship with the {@link User} entity.
     */
    @ManyToOne
    @JoinColumn(name = "consumer_id", nullable = false)
    private User owner;

    /**
     * The helper assigned to fulfill the request.
     * This establishes an optional many-to-one relationship with the {@link User} entity.
     */
    @ManyToOne
    @JoinColumn(name = "consumer_id_1")
    private User helper;

    /**
     * The category of the dog associated with the request.
     * This establishes a required many-to-one relationship with the {@link DogCategory} entity.
     */
    @ManyToOne
    @JoinColumn(name = "dog_category_id", nullable = false)
    private DogCategory dogCategory;

    /**
     * The state of the request, indicating whether it is active or completed.
     * This field is mandatory.
     */
    @Column(nullable = false)
    private Boolean state;
}
