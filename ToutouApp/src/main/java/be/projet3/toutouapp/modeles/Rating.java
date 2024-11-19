package be.projet3.toutouapp.modeles;

public class Rating {
    public int id;
    public int ratingValue;
    public String comment;

    public Rating(int id, int ratingValue, String comment) {
        this.id = id;
        this.ratingValue = ratingValue;
        this.comment = comment;
    }
}
