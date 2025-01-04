package be.projet3.toutouapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a category of dogs in the application.
 * This entity is used to classify dogs based on specific criteria,
 * such as size, breed type, or other characteristics.
 *
 * @see be.projet3.toutouapp.models
 * @author Damien De Leeuw
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DogCategory {

    /**
     * Unique identifier for the dog category.
     * It is automatically generated using an identity strategy.
     */
    @Id
    private Integer dogCategoryId;

    /**
     * The name of the category.
     * This field is mandatory and has a maximum length of 50 characters.
     * Examples include "Small Dogs", "Large Dogs", or "Sporting Breeds".
     */
    @Column(nullable = false, length = 50)
    private String category;
}
