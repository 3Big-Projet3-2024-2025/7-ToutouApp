package be.projet3.toutouapp.controllers;

import be.projet3.toutouapp.models.JWT;
import be.projet3.toutouapp.utils.JWTUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("login")
    public ResponseEntity<?> authenticate(@RequestParam String mail, @RequestParam String password) {
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(mail, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            User user = (User)auth.getPrincipal();
            JWT jwt = new JWT(jwtUtils.generateAccessToken(user),jwtUtils.generateRefreshToken(user));
            return ResponseEntity.ok(jwt);
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid mail or password");
        }
    }
}
