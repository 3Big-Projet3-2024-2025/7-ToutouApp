package be.projet3.toutouapp.dto;

import lombok.Data;

@Data
public class RatingInfoDTO {
    private Integer id;               // Ajout de l'ID
    private String evaluatedUserName; // Personne qui reçoit l'avis
    private String raterUserName;     // Personne qui donne l'avis
    private Integer ratingValue;      // Valeur de la note
    private String comment;           // Commentaire
    private String requestDate;       // Date de la requête
}
