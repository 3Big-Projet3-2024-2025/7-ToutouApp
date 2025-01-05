package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides methods for database operations on the Message table.
 *
 * @see be.projet3.toutouapp.repositories.jpa
 * @author Jaï Dusépulchre
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * Retrieves a list of {@link Message} entities associated with the given chat ID.
     *
     * @param chatId the ID of the chat to filter messages.
     * @return a list of messages associated with the specified chat ID.
     */
    List<Message> findByChat_ChatId(Integer chatId);
}
