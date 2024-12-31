package be.projet3.toutouapp.controllers;

import be.projet3.toutouapp.models.Role;
import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.repositories.jpa.RatingRepository;
import be.projet3.toutouapp.repositories.jpa.RoleRepository;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import be.projet3.toutouapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import be.projet3.toutouapp.services.IUserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Ajouter un nouvel utilisateur
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // Mettre à jour un utilisateur existant
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        // Vérifier si l'utilisateur existe
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));

        // Mettre à jour les informations de l'utilisateur
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setCountry(user.getCountry());
        existingUser.setCity(user.getCity());
        existingUser.setStreet(user.getStreet());
        existingUser.setPostalCode(user.getPostalCode());
        existingUser.setMail(user.getMail());

        // Mettre à jour le rôle si nécessaire (cela dépend si vous souhaitez permettre l'édition du rôle ou non)
        if (user.getRole() != null) {
            Role role = roleRepository.findById(user.getRole().getRoleId())
                    .orElseThrow(() -> new RuntimeException("Rôle non trouvé avec l'ID : " + user.getRole().getRoleId()));
            existingUser.setRole(role);
        }

        // Sauvegarder les modifications
        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }


    // Supprimer un utilisateur par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //attention pas le meme request mapping avant  @RequestMapping("/api/users")
    @PostMapping("/create")
    public ResponseEntity<User> createUserFromToken(JwtAuthenticationToken token) {
        System.out.println("Authorities : " + token.getAuthorities());
        Jwt jwt = token.getToken();
        User createdUser = userService.saveUserFromToken(jwt);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> emails = userService.getAllEmails();
        System.out.println("Requête GET /emails reçue");
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
