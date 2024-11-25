package be.projet3.toutouapp.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Boolean readed = false;

    @Column(nullable = false)
    private LocalDateTime messageTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "consumer_id", nullable = false)
    private User consumer;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
}

