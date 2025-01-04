package be.projet3.toutouapp.dto;

import lombok.Data;

@Data
public class RatingInfoDTO {
    private Integer id;
    private String evaluatedUserName;
    private String raterUserName;
    private Integer ratingValue;
    private String comment;
    private String requestDate;
}
