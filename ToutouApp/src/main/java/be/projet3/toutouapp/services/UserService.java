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
public class UserService implements UserDetailsService {

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




}
