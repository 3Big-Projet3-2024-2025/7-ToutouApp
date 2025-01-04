package be.projet3.toutouapp.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a rating entity in the application.
 * This entity stores feedback provided by a user, including a numeric rating and an optional comment,
 * related to a specific request.
 *
 * @see be.projet3.toutouapp.models
 * @author Damien De Leeuw
 */
@Entity
@Data
public class Rating {

    /**
     * Unique identifier for the rating.
     * Automatically generated using an identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ratingId;

    /**
     * The numeric value of the rating.
     * This field is mandatory and typically represents a satisfaction level
     * within a predefined range (e.g., 1 to 5).
     */
    @Column(nullable = false)
    private Integer ratingValue;

    /**
     * An optional comment provided by the user to give additional feedback.
     * The comment is limited to 500 characters.
     */
    @Column(length = 500)
    private String comment;

    /**
     * The request associated with this rating.
     * This establishes a required many-to-one relationship with the {@link Request} entity.
     */
    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    /**
     * The user (consumer) who provided the rating.
     * This establishes a required many-to-one relationship with the {@link User} entity.
     */
    @ManyToOne
    @JoinColumn(name = "consumer_id", nullable = false)
    private User consumer;
}
