package be.projet3.toutouapp.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ratingId;

    @Column(nullable = false)
    private Integer ratingValue;

    @Column(length = 500)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne
    @JoinColumn(name = "consumer_id", nullable = false)
    private User consumer;
}
