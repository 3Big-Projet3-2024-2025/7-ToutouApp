package be.projet3.toutouapp.services;

import be.projet3.toutouapp.models.Role;
import be.projet3.toutouapp.repositories.jpa.RoleRepository;
import ch.qos.logback.classic.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleById(int roleId) {
        Logger log = null;
        log.debug("Fetching role with ID: {}", roleId);
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent()) {
            log.debug("Role found: {}", role.get());
            return role.get();
        } else {
            log.error("Role with ID {} not found", roleId);
            throw new RuntimeException("Role not found");
        }
    }
}