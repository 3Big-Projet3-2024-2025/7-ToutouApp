package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByChat_ChatId(Integer chatId);
}
