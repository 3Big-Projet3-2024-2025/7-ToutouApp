package be.projet3.toutouapp.repositories.jpa;
import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Optional<Chat> findByRequest_RequestId(Integer requestId);
    Optional<Chat> findByChatId(Integer chatId);
}
