package be.projet3.toutouapp.modeles;

import java.time.LocalDate;

public class Chat {
    public int id;
    public LocalDate createDate;

    public Chat(int id, LocalDate createDate) {
        this.id = id;
        this.createDate = createDate;
    }
}
