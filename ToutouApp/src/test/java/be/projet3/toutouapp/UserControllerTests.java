package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.UserController;
import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.services.UserService;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
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

/**
 * These tests validate the functionality of user management endpoints in the controller.
 *
 * @see be.projet3.toutouapp
 * @author Jaï Dusépulchre
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    /**
     * Initializes mocks and sets up the test data before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // User initialization for testing
        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMail("johndoe@example.com");
    }

    /**
     * Tests the successful addition of a user.
     */
    @Test
    @Order(1)
    @DisplayName("Test successful addition of a user")
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

    /**
     * Tests the case when trying to add a user with invalid data.
     */
    @Test
    @Order(2)
    @DisplayName("Test adding a user with invalid data")
    public void testAddUser_BadRequest() {
        // Creation of an invalid user (for example, without email)
        User invalidUser = new User();
        invalidUser.setFirstName("John");
        invalidUser.setLastName("Doe");
        invalidUser.setMail("");  // Missing or invalid email

        when(userService.addUser(any(User.class))).thenThrow(new IllegalArgumentException("Invalid user data"));

        ResponseEntity<User> response = userController.addUser(invalidUser);

        assertEquals(400, response.getStatusCodeValue());  // Bad request
        assertNull(response.getBody());  // No body in the response
    }


    /**
     * Tests the case when adding a user with an invalid email format.
     */
    @Test
    @Order(3)
    @DisplayName("Test adding a user with an invalid email format")
    public void testAddUser_InvalidEmailFormat() {
        // Creation of a user with an incorrectly formatted email
        User invalidUser = new User();
        invalidUser.setFirstName("Jane");
        invalidUser.setLastName("Doe");
        invalidUser.setMail("invalid-email");  // Incorrect email format

        when(userService.addUser(any(User.class))).thenThrow(new IllegalArgumentException("Invalid email format"));

        ResponseEntity<User> response = userController.addUser(invalidUser);

        assertEquals(400, response.getStatusCodeValue());  // Bad request
        assertNull(response.getBody());  // No body in the response
    }


    /**
     * Tests the successful update of an existing user.
     */
    @Test
    @Order(4)
    @DisplayName("Test successful update of an existing user")
    public void testUpdateUser() throws Exception {
        // Simulation of existing user
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setMail("johndoe@example.com");

        // Simulation of updated user
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Smith");
        updatedUser.setMail("johnsmith@example.com");

        // Configuring mocks
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/1")
                        .contentType("application/json")
                        .content("{\"firstName\": \"John\", \"lastName\": \"Smith\", \"mail\": \"johnsmith@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.mail").value("johnsmith@example.com"));

        // Check that the mocked methods have been called
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Tests the case when attempting to update a user that does not exist.
     */
    @Test
    @Order(5)
    @DisplayName("Test updating a user that does not exist")
    public void testUpdateUser_NotFound() {
        int userId = 999;  // Non-existent user
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());  // The user does not exist

        ResponseEntity<User> response = userController.updateUser(userId, updatedUser);

        assertEquals(404, response.getStatusCodeValue());  // Not Found
        assertNull(response.getBody());  // No body in the response
    }


    /**
     * Tests the case when an internal server error occurs during user update.
     */
    @Test
    @Order(6)
    @DisplayName("Test internal server error during user update")
    public void testUpdateUser_InternalServerError() {
        int userId = 1;  // Existing user
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");

        when(userRepository.findById(userId)).thenThrow(new RuntimeException("Internal database error"));

        ResponseEntity<User> response = userController.updateUser(userId, updatedUser);

        assertEquals(500, response.getStatusCodeValue());  // Internal Server Error
        assertNull(response.getBody());  // No body in the response
    }


    /**
     * Tests the successful deletion of a user.
     */
    @Test
    @Order(7)
    @DisplayName("Test successful deletion of a user")
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1);
    }

    /**
     * Tests the case when attempting to delete a non-existent user.
     */
    @Test
    @Order(8)
    @DisplayName("Test deleting a user that does not exist")
    public void testDeleteUser_NotFound() {
        int userId = 999;  // Non-existent user

        doThrow(new RuntimeException("User not found with ID : " + userId)).when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(404, response.getStatusCodeValue());  // Not Found
    }


    /**
     * Tests the case when an internal server error occurs during user deletion.
     */
    @Test
    @Order(9)
    @DisplayName("Test internal server error during user deletion")
    public void testDeleteUser_InternalServerError() {
        int userId = 1;  // Existing user

        doThrow(new RuntimeException("Internal database error")).when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(500, response.getStatusCodeValue());  // Internal Server Error
    }


    /**
     * Tests the successful retrieval of a user by email.
     */
    @Test
    @Order(10)
    @DisplayName("Test successful retrieval of a user by email")
    public void testGetUserByEmail() throws Exception {
        when(userService.getUserByEmail("johndoe@example.com")).thenReturn(user);

        mockMvc.perform(get("/user/johndoe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.mail").value("johndoe@example.com"));

        verify(userService, times(1)).getUserByEmail("johndoe@example.com");
    }

    /**
     * Tests the case when a user is not found by email.
     */
    @Test
    @Order(11)
    @DisplayName("Test retrieving a user by email when user is not found")
    public void testGetUserByEmail_NotFound() throws Exception {
        // Simulation: No user found with this email
        when(userService.getUserByEmail("unknown@example.com")).thenThrow(new RuntimeException("User not found with email : unknown@example.com"));

        mockMvc.perform(get("/user/unknown@example.com"))
                .andExpect(status().isNotFound());

        // Vérifie que la méthode getUserByEmail est bien appelée avec le bon email
        verify(userService, times(1)).getUserByEmail("unknown@example.com");
    }
}
