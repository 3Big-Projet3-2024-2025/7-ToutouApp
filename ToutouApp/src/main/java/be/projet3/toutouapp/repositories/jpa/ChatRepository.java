package be.projet3.toutouapp.repositories.jpa;
import be.projet3.toutouapp.models.Chat;
import be.projet3.toutouapp.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Provides methods for database operations on the Chat table.
 *
 * @see be.projet3.toutouapp.repositories.jpa
 * @author Jaï Dusépulchre
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    /**
     * Finds a chat by the associated request ID.
     *
     * @param requestId the ID of the associated request.
     * @return an {@link Optional} containing the found {@link Chat},
     * or an empty {@link Optional} if no chat is found.
     */
    Optional<Chat> findByRequest_RequestId(Integer requestId);


    /**
     * Finds a chat by its ID.
     *
     * @param chatId the ID of the chat to find.
     * @return an {@link Optional} containing the found {@link Chat},
     * or an empty {@link Optional} if no chat is found.
     */
    Optional<Chat> findByChatId(Integer chatId);
}
