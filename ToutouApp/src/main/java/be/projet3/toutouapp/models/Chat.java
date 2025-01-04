package be.projet3.toutouapp.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a chat entity in the application.
 * This entity is linked to a specific request and contains metadata
 * about the chat's creation date and status.
 *
 * @see be.projet3.toutouapp.models
 * @author Damien De Leeuw
 */
@Entity
@Data
public class Chat {

    /**
     * Unique identifier for the chat.
     * Automatically generated using an identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    /**
     * The timestamp indicating when the chat was created.
     * This field is mandatory and is initialized to the current time by default.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * A flag indicating whether the chat is active.
     * This field is mandatory and defaults to {@code true}.
     */
    @Column(nullable = false)
    private Boolean chatFlag = true;

    /**
     * The request associated with this chat.
     * This establishes a required one-to-one relationship with the {@link Request} entity.
     * The `request_id` column in the database ensures that each chat is linked to a unique request.
     */
    @OneToOne
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    private Request request;
}
