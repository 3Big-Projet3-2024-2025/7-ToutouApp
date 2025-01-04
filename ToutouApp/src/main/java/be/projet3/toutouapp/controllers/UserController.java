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
import java.util.Map;

/**
 * REST controller for managing users.
 * Provides endpoints for CRUD operations, user activation/deactivation,
 * and role management.
 *
 * @see be.projet3.toutouapp.controllers
 * @author Jaï Dusépulchre
 */
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

    /**
     * Retrieves all active users.
     *
     * @return ResponseEntity containing a list of active users and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> activeUsers = userService.getAllUsers()
                .stream()
                .filter(User::isActive) // Filtrer par userFlag = true
                .toList();
        return ResponseEntity.ok(activeUsers);
    }

    /**
     * Adds a new user.
     *
     * @param user the User object to be created.
     * @return ResponseEntity containing the created User and HTTP status:
     * - 201 (Created) if successful,
     * - 400 (Bad Request) if there is an error in the request.
     */
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            User newUser = userService.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Updates an existing user by ID.
     *
     * @param id   the ID of the user to be updated.
     * @param user the User object containing updated details.
     * @return ResponseEntity containing the updated User and HTTP status:
     * - 200 (OK) if successful,
     * - 404 (Not Found) if the user does not exist,
     * - 500 (Internal Server Error) for unexpected errors.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            // Check if user exists
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with ID : " + id));

            // Update user information
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setCountry(user.getCountry());
            existingUser.setCity(user.getCity());
            existingUser.setStreet(user.getStreet());
            existingUser.setPostalCode(user.getPostalCode());
            existingUser.setMail(user.getMail());

            // Update the role if necessary
            if (user.getRole() != null) {
                Role role = roleRepository.findById(user.getRole().getRoleId())
                        .orElseThrow(() -> new RuntimeException("Role not found with ID : " + user.getRole().getRoleId()));
                existingUser.setRole(role);
            }

            // Save changes
            User updatedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            // If the user is not found or other error
            if (e.getMessage().contains("User not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Handle unexpected errors like 500 errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted.
     * @return ResponseEntity with HTTP status:
     * - 204 (No Content) if deletion is successful,
     * - 404 (Not Found) if the user does not exist,
     * - 500 (Internal Server Error) for unexpected errors.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // If the user is not found or other error
            if (e.getMessage().contains("User not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // Handle unexpected errors like 500 errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Creates a user from the given authentication token.
     *
     * @param token the JwtAuthenticationToken containing user information.
     * @return ResponseEntity containing the created User and HTTP status 200 (OK).
     */
    @PostMapping("/create")
    public ResponseEntity<User> createUserFromToken(JwtAuthenticationToken token) {
        System.out.println("Authorities : " + token.getAuthorities());
        Jwt jwt = token.getToken();
        User createdUser = userService.saveUserFromToken(jwt);
        return ResponseEntity.ok(createdUser);
    }

    /**
     * Retrieves all user emails.
     *
     * @return ResponseEntity containing a list of email strings and HTTP status 200 (OK).
     */
    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> emails = userService.getAllEmails();
        System.out.println("Requête GET /emails reçue");
        return ResponseEntity.ok(emails);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve.
     * @return ResponseEntity containing the User and HTTP status:
     * - 200 (OK) if the user is found,
     * - 404 (Not Found) if the user does not exist.
     */
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Blocks or unblocks a user.
     *
     * @param id    the ID of the user to block/unblock.
     * @param block the boolean flag indicating whether to block (true) or unblock (false) the user.
     * @return ResponseEntity containing the updated User and HTTP status:
     * - 200 (OK) if successful,
     * - 400 (Bad Request) if blocking is not allowed or other issues occur.
     */
    @PatchMapping("/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable int id, @RequestParam boolean block) {
        try {
            User user = userService.getUserById(id);

            // If the user is an ADMIN and is the last active one
            if (block && "ADMIN".equals(user.getRole().getName())) {
                long activeAdminCount = userService.countActiveAdmins();
                if (activeAdminCount <= 1) {
                    throw new RuntimeException("Cannot block the last active administrator!");
                }
            }

            // Update the block status
            user.setBlocked(block);

            // Save the changes
            User updatedUser = userService.updateUser(user);

            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            System.err.println("Error while blocking/unblocking user: " + e.getMessage());

            // Return a well-structured JSON response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Activates or deactivates a user.
     *
     * @param id   the ID of the user to activate/deactivate.
     * @param flag the boolean flag indicating whether to activate (true) or deactivate (false) the user.
     * @return ResponseEntity containing the updated User and HTTP status:
     * - 200 (OK) if successful,
     * - 400 (Bad Request) if deactivation is not allowed or other issues occur.
     */
    @PatchMapping("/{id}/flag")
    public ResponseEntity<?> updateUserFlag(@PathVariable int id, @RequestParam boolean flag) {
        try {
            User user = userService.getUserById(id);

            // Check if the user is linked to an active request
            if (userService.isUserLinkedToActiveRequests(id) && !flag) {
                // If the user is linked to an active request, prevent deactivation
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Cannot deactivate a user linked to an active request."));
            }

            // If the user is an ADMIN and is the last active one
            if (!flag && "ADMIN".equals(user.getRole().getName())) {
                long activeAdminCount = userService.countActiveAdmins();
                if (activeAdminCount <= 1) {
                    throw new RuntimeException("Cannot deactivate the last active administrator!");
                }
            }

            // Update the userFlag property
            user.setActive(flag);

            // Save the changes
            User updatedUser = userService.updateUser(user);

            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            System.err.println("Error while updating user flag: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
