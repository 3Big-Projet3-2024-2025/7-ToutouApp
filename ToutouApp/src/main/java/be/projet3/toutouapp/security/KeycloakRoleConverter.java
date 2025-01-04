package be.projet3.toutouapp.security;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converter for extracting roles from a JWT token and converting them into a collection of {@link GrantedAuthority}.
 * This class is used to convert Keycloak roles into Spring Security authorities.
 * Implements the {@link Converter} interface from Spring Security to perform the conversion.
 * @author Damien DeLeeuw
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {


    /**
     * Converts a JWT token into a collection of {@link GrantedAuthority} by extracting the roles from the token.
     * The roles are expected to be contained within the "realm_access" claim of the JWT.
     * Each role is prefixed with "ROLE_" and mapped to a {@link SimpleGrantedAuthority}.
     *
     * @param jwt the JWT token that contains the roles in the "realm_access" claim
     * @return a collection of {@link GrantedAuthority} representing the roles
     * @author Damien DeLeeuw
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        if (realmAccess == null || realmAccess.get("roles") == null) {
            return List.of();
        }

        Collection<String> roles = (Collection<String>) realmAccess.get("roles");

        System.out.println("Rôles extraits du token JWT : " + roles); // ajout test

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }
}
