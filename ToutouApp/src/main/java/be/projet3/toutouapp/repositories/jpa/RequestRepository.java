package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByOwner_Id(int ownerId);

}
