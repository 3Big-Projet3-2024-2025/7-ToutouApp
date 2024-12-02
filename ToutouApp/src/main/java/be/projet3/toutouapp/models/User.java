package be.projet3.toutouapp.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "consumer")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumer_id") // Correspond à la colonne consumer_id dans la base de données
    private int id;

    @Column(name = "mail", nullable = false) // Correspond à la colonne mail
    private String mail;

    @Column(name = "password", nullable = false) // Correspond à la colonne password
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "last_name", nullable = false, length = 50) // Correspond à la colonne last_name
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 50) // Correspond à la colonne first_name
    private String firstName;

    @Column(name = "country", nullable = false, length = 50) // Correspond à la colonne country
    private String country;

    @Column(name = "city", nullable = false, length = 50) // Correspond à la colonne city
    private String city;

    @Column(name = "street", nullable = false, length = 100) // Correspond à la colonne street
    private String street;

    @Column(name = "postal_code", nullable = false, length = 20) // Correspond à la colonne postal_code
    private String postalCode;

    @Column(name = "user_flag", nullable = false) // Correspond à la colonne user_flag
    private boolean active = true;

    @Column(name = "is_blocked", nullable = false) // Correspond à la colonne is_blocked
    private boolean blocked = false;

    @ManyToOne() // Relation avec la table Role
    @JoinColumn(name = "roleId", referencedColumnName = "roleId", nullable = false)
    private Role role; // La classe Role doit être définie pour la table Role

}

