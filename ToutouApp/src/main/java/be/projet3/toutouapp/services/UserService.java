package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Request;
import be.projet3.toutouapp.models.Role;
import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.repositories.jpa.RequestRepository;
import be.projet3.toutouapp.repositories.jpa.RoleRepository;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing {@link User} entities.
 * Implements {@link UserDetailsService} to load user details for authentication.
 *
 *@see be.projet3.toutouapp.services
 *@author Jaï Dusépulchre
 */
@Service
public class UserService implements UserDetailsService, IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RequestRepository requestRepository;

    /**
     * Loads the user details by username (email).
     *
     * @param mail the email of the user to retrieve.
     * @return a {@link UserDetails} object containing user information.
     * @throws UsernameNotFoundException if the user is not found by the provided email.
     */
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userRepository.findByMail((mail));
        if(user == null) {
            throw new UsernameNotFoundException(mail);
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getMail(), user.getPassword(), getGrantedAuthorities(user.getRole().getName()));
        return userDetails;
    }

    /**
     * Retrieves granted authorities based on the user's role.
     *
     * @param role the role of the user.
     * @return a list of {@link GrantedAuthority} objects for the user's role.
     */
    public List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.toUpperCase()));
        return authorities;
    }

    /**
     * Saves a new user using information extracted from a JWT.
     *
     * @param jwt the JWT containing the user's information.
     * @return the saved {@link User}.
     * @throws RuntimeException if the user already exists or the role is not found.
     */
    public User saveUserFromToken(Jwt jwt) {
        // Extraire les informations du token
        String email = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");
        String country = jwt.getClaimAsString("country");
        String city = jwt.getClaimAsString("city");
        String street = jwt.getClaimAsString("street");
        String postalCode = jwt.getClaimAsString("postal_code");

        // Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByMail(email)) {
            throw new RuntimeException("Utilisateur déjà existant avec cet email : " + email);
        }

        // Récupérer le rôle par défaut (supposons "USER")
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rôle USER non trouvé"));

        // Créer un nouvel utilisateur
        User user = new User();
        user.setMail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCountry(country);
        user.setCity(city);
        user.setStreet(street);
        user.setPostalCode(postalCode);
        user.setPassword("N/A"); // Pas besoin de mot de passe dans ce cas, car géré par Keycloak
        user.setRole(role);

        // Sauvegarder l'utilisateur
        return userRepository.save(user);
    }

    /**
     * Retrieves all emails of the users.
     *
     * @return a list of user emails.
     */
    public List<String> getAllEmails() {
        return userRepository.findAll()
                .stream()
                .map(User::getMail)
                .collect(Collectors.toList());
    }

    /**
     * Adds a new user to the repository.
     *
     * @param user the user to be added.
     * @return the added {@link User}.
     */
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all {@link User} entities.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user.
     * @return the {@link User} with the given ID.
     * @throws RuntimeException if the user is not found.
     */
    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID : " + id));
    }

    /**
     * Updates an existing user.
     *
     * @param user the user with updated information.
     * @return the updated {@link User}.
     * @throws RuntimeException if the user is not found or the role is not valid.
     */
    @Override
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("User not found with ID : " + user.getId());
        }

        if (user.getRole() == null) {
            throw new RuntimeException("User role cannot be null");
        }

        // If the user has a "USER" or "ADMIN" role, we update it with this role
        Role role = roleRepository.findByName(user.getRole().getName())
                .orElseThrow(() -> new RuntimeException("Role not found : " + user.getRole().getName()));

        // Update user role
        user.setRole(role);

        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     * @throws RuntimeException if the user is not found.
     */
    @Override
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID : " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user.
     * @return the {@link User} associated with the email.
     * @throws RuntimeException if the user is not found.
     */
    public User getUserByEmail(String email) {
        User user = userRepository.findByMail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email : " + email);
        }
        return user;
    }

    /**
     * Retrieves all active users.
     *
     * @return a list of active {@link User} entities.
     */
    public List<User> getActiveUsers() {
        return userRepository.findAll()
                .stream()
                .filter(User::isActive) // Filtrer par userFlag = true
                .collect(Collectors.toList());
    }

    /**
     * Counts the number of active administrators.
     *
     * @return the count of active administrators.
     */
    public long countActiveAdmins() {
        return userRepository.findAll()
                .stream()
                .filter(user -> "ADMIN".equals(user.getRole().getName()) && user.isActive())
                .count();
    }

    /**
     * Checks if a user is linked to any active requests.
     *
     * @param userId the ID of the user.
     * @return true if the user is linked to any active requests, false otherwise.
     */
    public boolean isUserLinkedToActiveRequests(int userId) {
        List<Request> userRequests = requestRepository.findByOwner_Id(userId);

        // Check if the user is linked to an active request (state == false)
        for (Request request : userRequests) {
            if (!request.getState()) { // If the request is active (state == false)
                return true; // The user is linked to an active request
            }
        }

        return false; // The user is not linked to any active requests
    }

    /**
     * Retrieves the current authenticated user.
     *
     * @return the current {@link User} based on the authentication token.
     * @throws RuntimeException if the user is not authenticated.
     */
     public User getCurrentUser() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authenticationToken == null || authenticationToken.getToken() == null) {
            throw new RuntimeException("Unauthenticated user");
        }

        String email = authenticationToken.getToken().getClaimAsString("email");
        System.out.println("Authenticated user : " + email);

        return userRepository.findByMail(email);
    }
}
