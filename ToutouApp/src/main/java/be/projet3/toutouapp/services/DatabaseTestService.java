package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseTestService {

    @Autowired
    private UserRepository userRepo;

    public String testDatabaseConnection() {
        try {
            int id = 1 ;
            // Essayer de récupérer un utilisateur depuis la base de données
            User user = userRepo.findById(id).orElse(null);
            if (user != null) {
                return "Connection successful, found user: " + user.getMail()+" "  + user.getRole().getName();
            } else {
                return "Connection successful, no user found.";
            }
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
}
