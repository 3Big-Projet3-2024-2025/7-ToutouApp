package be.projet3.toutouapp.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a message entity in the application.
 * This entity stores the details of messages exchanged within a chat, including content,
 * timestamp, and status information.
 *
 * @see be.projet3.toutouapp.models
 * @author Damien De Leeuw
 */
@Entity
@Data
public class Message {

    /**
     * Unique identifier for the message.
     * Automatically generated using an identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    /**
     * The content of the message.
     * This field is mandatory and has a maximum length of 500 characters.
     */
    @Column(nullable = false, length = 500)
    private String content;

    /**
     * A flag indicating whether the message has been read.
     * By default, this value is set to {@code false}.
     */
    @Column(nullable = false)
    private Boolean readed = false;

    /**
     * The timestamp indicating when the message was sent.
     * This field is mandatory and is initialized to the current time by default.
     */
    @Column(nullable = false)
    private LocalDateTime messageTime = LocalDateTime.now();

    /**
     * The user (consumer) associated with the message.
     * This establishes a required many-to-one relationship with the {@link User} entity,
     * representing either the sender or recipient of the message.
     */
    @ManyToOne
    @JoinColumn(name = "consumer_id", nullable = false)
    private User consumer;

    /**
     * The chat session to which this message belongs.
     * This establishes a required many-to-one relationship with the {@link Chat} entity.
     */
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
}
