package be.projet3.toutouapp.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DogCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dogCategoryId;

    @Column(nullable = false, length = 50)
    private String category;
}