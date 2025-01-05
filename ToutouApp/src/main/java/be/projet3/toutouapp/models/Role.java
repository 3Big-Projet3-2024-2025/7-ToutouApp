package be.projet3.toutouapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a role entity in the application.
 * This entity is used to manage user roles, which define permissions and access levels within the system.
 * Common examples include "Admin", "User".
 *
 * @see be.projet3.toutouapp.models
 * @author Damien De Leeuw
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    /**
     * Unique identifier for the role.
     * This is automatically generated using an identity strategy.
     */
    @Id
    private Integer roleId;

    /**
     * The name of the role.
     * This field is mandatory and has a maximum length of 50 characters.
     * It represents the descriptive name of the role (e.g., "Admin", "User").
     */
    @Column(nullable = false, length = 50)
    private String name;
}
