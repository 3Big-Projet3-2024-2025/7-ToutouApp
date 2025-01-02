package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
}
