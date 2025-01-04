package be.projet3.toutouapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DogCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dogCategoryId;

    @Column(nullable = false, length = 50)
    private String category;
}