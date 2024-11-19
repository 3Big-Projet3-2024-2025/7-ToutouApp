package be.projet3.toutouapp.modeles;

import java.time.LocalDate;

public class Request {
    public int id;
    public LocalDate requestDate;
    public String photo;
    public String category;
    public String dogName;
    public LocalDate startTime;
    public LocalDate endTime;


    public Request(int id, LocalDate requestDate, String photo, String category, String dogName, LocalDate startTime, LocalDate endTime) {
        this.id = id;
        this.requestDate = requestDate;
        this.photo = photo;
        this.category = category;
        this.dogName = dogName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
