package be.projet3.toutouapp.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean chatFlag = true;

    @OneToOne
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    private Request request;
}
