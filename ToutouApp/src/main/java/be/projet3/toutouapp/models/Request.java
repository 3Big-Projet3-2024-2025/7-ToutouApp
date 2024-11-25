package be.projet3.toutouapp.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @Column(nullable = false)
    private LocalDate requestDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, length = 50)
    private String dogName;

    @Column(length = 300)
    private String photo;

    @Column(length = 500)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "consumer_id_1", nullable = false)
    private User helper;

    @ManyToOne
    @JoinColumn(name = "dog_category_id", nullable = false)
    private DogCategory dogCategory;
}
