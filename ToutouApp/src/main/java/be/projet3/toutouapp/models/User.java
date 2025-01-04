package be.projet3.toutouapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a user entity in the application.
 * This entity stores information about application users, including their personal details,
 * address, and account status.
 *
 * @see be.projet3.toutouapp.models
 * @author Damien De Leeuw (la228388)
 */
@Entity
@Data
@Table(name = "consumer")
public class User {

    /**
     * Unique identifier for the user.
     * This maps to the `consumer_id` column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumer_id")
    private int id;

    /**
     * Email address of the user.
     * This field is required and maps to the `mail` column in the database.
     */
    @Column(name = "mail", nullable = false)
    private String mail;

    /**
     * Password of the user.
     * This field is required and maps to the `password` column in the database.
     * The password is excluded from being serialized in JSON responses.
     */
    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Last name of the user.
     * This field is required and maps to the `last_name` column in the database.
     * The maximum length is 50 characters.
     */
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    /**
     * First name of the user.
     * This field is required and maps to the `first_name` column in the database.
     * The maximum length is 50 characters.
     */
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    /**
     * Country of residence of the user.
     * This field is required and maps to the `country` column in the database.
     * The maximum length is 50 characters.
     */
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    /**
     * City of residence of the user.
     * This field is required and maps to the `city` column in the database.
     * The maximum length is 50 characters.
     */
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    /**
     * Street address of the user.
     * This field is required and maps to the `street` column in the database.
     * The maximum length is 100 characters.
     */
    @Column(name = "street", nullable = false, length = 100)
    private String street;

    /**
     * Postal code of the user's address.
     * This field is required and maps to the `postal_code` column in the database.
     * The maximum length is 20 characters.
     */
    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    /**
     * Flag indicating whether the user account is active.
     * This field is required and maps to the `user_flag` column in the database.
     * By default, it is set to {@code true}.
     */
    @Column(name = "user_flag", nullable = false)
    private boolean active = true;

    /**
     * Flag indicating whether the user account is blocked.
     * This field is required and maps to the `is_blocked` column in the database.
     * By default, it is set to {@code false}.
     */
    @Column(name = "is_blocked", nullable = false)
    private boolean blocked = false;

    /**
     * Role assigned to the user.
     * This establishes a many-to-one relationship with the {@link Role} entity.
     * The role determines the user's permissions and access level within the application.
     */
    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "roleId", nullable = false)
    private Role role;
}
