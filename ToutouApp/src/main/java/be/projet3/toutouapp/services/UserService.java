package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userRepo.findByMail((mail));
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



}
