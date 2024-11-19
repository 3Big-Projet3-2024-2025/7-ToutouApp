package be.projet3.toutouapp.modeles;

import lombok.Getter;

public class Consumer {
    public int id;
    public String firstName;
    public String lastName;
    public String mail;
    @Getter
    private String password;
    public String country;
    public String city;
    public String street;
    public String postCode;
    public String number;

    public Consumer(int id, String firstName, String lastName, String mail, String password, String country, String city, String street, String postCode, String number) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
        this.country = country;
        this.city = city;
        this.street = street;
        this.postCode = postCode;
        this.number = number;
    }
}
