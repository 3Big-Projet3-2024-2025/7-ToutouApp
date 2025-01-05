package be.projet3.toutouapp.dto;

import lombok.Data;


/**
 * @see be.projet3.toutouapp.dto
 * Data Transfer Object (DTO) for holding rating information.
 * This DTO is used to transfer information about a rating made by a user
 * for another user (evaluated user) along with a comment and the request date.
 */
@Data
public class RatingInfoDTO {
    private Integer id;
    private String evaluatedUserName;
    private String raterUserName;
    private Integer ratingValue;
    private String comment;
    private String requestDate;
}
