package be.projet3.toutouapp.controllers;

import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CREATE: Ajouter un utilisateur
    @PostMapping
    public User addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // READ: Récupérer tous les utilisateurs
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // READ: Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));
    }

    // UPDATE: Mettre à jour un utilisateur
    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));

        // Mettre à jour les champs
        existingUser.setMail(updatedUser.getMail());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setCountry(updatedUser.getCountry());
        existingUser.setCity(updatedUser.getCity());
        existingUser.setStreet(updatedUser.getStreet());
        existingUser.setPostalCode(updatedUser.getPostalCode());
        existingUser.setActive(updatedUser.isActive());
        existingUser.setBlocked(updatedUser.isBlocked());
        existingUser.setRole(updatedUser.getRole());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepo.save(existingUser);
    }

    // DELETE: Supprimer un utilisateur par ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("Utilisateur introuvable avec l'ID : " + id);
        }
        userRepo.deleteById(id);
    }
}
