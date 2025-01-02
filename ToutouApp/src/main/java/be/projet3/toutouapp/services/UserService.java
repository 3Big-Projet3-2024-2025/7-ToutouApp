package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Role;
import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.repositories.jpa.RoleRepository;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService, IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userRepository.findByMail((mail));
        if(user == null) {
            throw new UsernameNotFoundException(mail);
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getMail(), user.getPassword(), getGrantedAuthorities(user.getRole().getName()));
        return userDetails;
    }

    public List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.toUpperCase()));
        return authorities;
    }


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
    public List<String> getAllEmails() {
        return userRepository.findAll()
                .stream()
                .map(User::getMail)
                .collect(Collectors.toList());
    }


    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));
    }

    @Override
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("Utilisateur introuvable avec l'ID : " + user.getId());
        }

        if (user.getRole() == null) {
            throw new RuntimeException("Le rôle de l'utilisateur ne peut être nul");
        }

        // Si l'utilisateur a un rôle "USER" ou "ADMIN", on le met à jour avec ce rôle
        Role role = roleRepository.findByName(user.getRole().getName())
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé : " + user.getRole().getName()));

        // Mise à jour du rôle de l'utilisateur
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur introuvable avec l'ID : " + id);
        }
        userRepository.deleteById(id);
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByMail(email);
        if (user == null) {
            throw new RuntimeException("Utilisateur introuvable avec l'email : " + email);
        }
        return user;
    }

    public List<User> getActiveUsers() {
        return userRepository.findAll()
                .stream()
                .filter(User::isActive) // Filtrer par userFlag = true
                .collect(Collectors.toList());
    }

    public long countActiveAdmins() {
        return userRepository.findAll()
                .stream()
                .filter(user -> "ADMIN".equals(user.getRole().getName()) && user.isActive())
                .count();
    }


}
