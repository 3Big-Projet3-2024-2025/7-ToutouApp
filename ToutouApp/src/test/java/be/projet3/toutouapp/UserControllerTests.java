package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.UserController;
import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.services.UserService;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Initialisation de l'utilisateur pour les tests
        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMail("johndoe@example.com");
    }

    @Test
    public void testAddUser() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"mail\": \"johndoe@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.mail").value("johndoe@example.com"));

        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    public void testAddUser_BadRequest() {
        // Création d'un utilisateur invalide (par exemple, sans email)
        User invalidUser = new User();
        invalidUser.setFirstName("John");
        invalidUser.setLastName("Doe");
        invalidUser.setMail("");  // Email manquant ou invalide

        when(userService.addUser(any(User.class))).thenThrow(new IllegalArgumentException("Données utilisateur invalides"));

        ResponseEntity<User> response = userController.addUser(invalidUser);

        assertEquals(400, response.getStatusCodeValue());  // Mauvaise requête
        assertNull(response.getBody());  // Aucun corps dans la réponse
    }


    @Test
    public void testAddUser_InvalidEmailFormat() {
        // Création d'un utilisateur avec un email au format incorrect
        User invalidUser = new User();
        invalidUser.setFirstName("Jane");
        invalidUser.setLastName("Doe");
        invalidUser.setMail("invalid-email");  // Format d'email incorrect

        when(userService.addUser(any(User.class))).thenThrow(new IllegalArgumentException("Format d'email invalide"));

        ResponseEntity<User> response = userController.addUser(invalidUser);

        assertEquals(400, response.getStatusCodeValue());  // Mauvaise requête
        assertNull(response.getBody());  // Aucun corps dans la réponse
    }


    @Test
    public void testUpdateUser() throws Exception {
        // Simulation de l'utilisateur existant
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setMail("johndoe@example.com");

        // Simulation de l'utilisateur mis à jour
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Smith");
        updatedUser.setMail("johnsmith@example.com");

        // Configuration des mocks
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/1")
                        .contentType("application/json")
                        .content("{\"firstName\": \"John\", \"lastName\": \"Smith\", \"mail\": \"johnsmith@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.mail").value("johnsmith@example.com"));

        // Vérifier que les méthodes mockées ont bien été appelées
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser_NotFound() {
        int userId = 999;  // Utilisateur inexistant
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());  // L'utilisateur n'existe pas

        ResponseEntity<User> response = userController.updateUser(userId, updatedUser);

        assertEquals(404, response.getStatusCodeValue());  // Not Found
        assertNull(response.getBody());  // Aucun corps dans la réponse
    }


    @Test
    public void testUpdateUser_InternalServerError() {
        int userId = 1;  // Utilisateur existant
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");

        when(userRepository.findById(userId)).thenThrow(new RuntimeException("Erreur interne de la base de données"));

        ResponseEntity<User> response = userController.updateUser(userId, updatedUser);

        assertEquals(500, response.getStatusCodeValue());  // Erreur interne du serveur
        assertNull(response.getBody());  // Aucun corps dans la réponse
    }


    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    public void testDeleteUser_NotFound() {
        int userId = 999;  // Utilisateur inexistant

        doThrow(new RuntimeException("Utilisateur introuvable avec l'ID : " + userId)).when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(404, response.getStatusCodeValue());  // Not Found
    }


    @Test
    public void testDeleteUser_InternalServerError() {
        int userId = 1;  // Utilisateur existant

        doThrow(new RuntimeException("Erreur interne de la base de données")).when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(500, response.getStatusCodeValue());  // Erreur interne du serveur
    }



    @Test
    public void testGetUserByEmail() throws Exception {
        when(userService.getUserByEmail("johndoe@example.com")).thenReturn(user);

        mockMvc.perform(get("/user/johndoe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.mail").value("johndoe@example.com"));

        verify(userService, times(1)).getUserByEmail("johndoe@example.com");
    }

    @Test
    public void testGetUserByEmail_NotFound() throws Exception {
        // Simulation : Aucun utilisateur trouvé avec cet email
        when(userService.getUserByEmail("unknown@example.com")).thenThrow(new RuntimeException("Utilisateur introuvable avec l'email : unknown@example.com"));

        mockMvc.perform(get("/user/unknown@example.com"))
                .andExpect(status().isNotFound());

        // Vérifie que la méthode getUserByEmail est bien appelée avec le bon email
        verify(userService, times(1)).getUserByEmail("unknown@example.com");
    }
}
