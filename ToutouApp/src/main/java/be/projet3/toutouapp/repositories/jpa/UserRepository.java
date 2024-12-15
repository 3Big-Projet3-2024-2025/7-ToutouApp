package be.projet3.toutouapp.repositories.jpa;

import be.projet3.toutouapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByMail(String mail);
    boolean existsByMail(String mail);
}

