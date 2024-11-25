package be.projet3.toutouapp.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer participantId;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "consumer_id", nullable = false)
    private User consumer;
}
