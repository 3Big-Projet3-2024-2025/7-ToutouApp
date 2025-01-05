package be.projet3.toutouapp;

import be.projet3.toutouapp.controllers.UserController;
import be.projet3.toutouapp.models.Role;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

/**
 * These tests validate the functionality of user management endpoints in the controller.
 *
 * @see be.projet3.toutouapp
 * @author Jaï Dusépulchre, Sirjacques Célestin
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
        user.setBlocked(false);
        user.setActive(true);

        Role role = new Role();
        role.setName("USER");
        user.setRole(role);
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

    /**
     * Test blocking a user successfully.
     * Verifies that the user is blocked and the correct response is returned.
     */
    @Test
    public void testBlockUser() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);
        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(patch("/user/1/block").param("block", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blocked").value(true));

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).updateUser(any(User.class));
    }

    /**
     * Test blocking the last active administrator.
     * Verifies that the action is rejected with an error message.
     */
    @Test
    public void testBlockUser_LastAdmin() throws Exception {
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        user.setRole(adminRole);

        when(userService.getUserById(1)).thenReturn(user);
        when(userService.countActiveAdmins()).thenReturn(1L);

        mockMvc.perform(patch("/user/1/block").param("block", "true"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Cannot block the last active administrator!"));

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).countActiveAdmins();
        verify(userService, never()).updateUser(any(User.class));
    }

    /**
     * Test updating the user flag (activation/deactivation).
     * Verifies that the user's active flag is correctly updated.
     */
    @Test
    public void testUpdateUserFlag() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);
        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(patch("/user/1/flag").param("flag", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).updateUser(any(User.class));
    }

    /**
     * Test updating the user flag when the user is linked to active requests.
     * Verifies that the action is rejected with an error message.
     */
    @Test
    public void testUpdateUserFlag_LinkedToActiveRequests() throws Exception {
        when(userService.getUserById(1)).thenReturn(user);
        when(userService.isUserLinkedToActiveRequests(1)).thenReturn(true);

        mockMvc.perform(patch("/user/1/flag").param("flag", "false"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Cannot deactivate a user linked to an active request."));

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).isUserLinkedToActiveRequests(1);
        verify(userService, never()).updateUser(any(User.class));
    }

    /**
     * Test updating the user flag when the user is the last active administrator.
     * Verifies that the action is rejected with an error message.
     */
    @Test
    public void testUpdateUserFlag_LastAdmin() throws Exception {
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        user.setRole(adminRole);

        when(userService.getUserById(1)).thenReturn(user);
        when(userService.countActiveAdmins()).thenReturn(1L);

        mockMvc.perform(patch("/user/1/flag").param("flag", "false"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Cannot deactivate the last active administrator!"));

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).countActiveAdmins();
        verify(userService, never()).updateUser(any(User.class));
    }

    /**
     * Test unblocking a user.
     * Verifies that the user is unblocked and the correct response is returned.
     */
    @Test
    public void testUnblockUser() throws Exception {
        // Simulate a blocked user
        user.setBlocked(true);

        when(userService.getUserById(1)).thenReturn(user);
        when(userService.updateUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Returns the updated user

        mockMvc.perform(patch("/user/1/block").param("block", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blocked").value(false));

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).updateUser(any(User.class));
    }

    /**
     * Test activating a user.
     * Verifies that the user's active flag is correctly set to true.
     */
    @Test
    public void testActivateUser() throws Exception {
        // Simulate a deactivated user
        user.setActive(false);

        when(userService.getUserById(1)).thenReturn(user);
        when(userService.updateUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Returns the updated user

        mockMvc.perform(patch("/user/1/flag").param("flag", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).updateUser(any(User.class));
    }

    /**
     * Test blocking a user with an invalid parameter.
     * Verifies that a bad request status is returned when no parameter is provided.
     */
    @Test
    public void testBlockUser_InvalidParameter() throws Exception {
        mockMvc.perform(patch("/user/1/block"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(anyInt());
        verify(userService, never()).updateUser(any(User.class));
    }

    /**
     * Test updating the user flag with an invalid parameter.
     * Verifies that a bad request status is returned when no parameter is provided.
     */
    @Test
    public void testUpdateUserFlag_InvalidParameter() throws Exception {
        mockMvc.perform(patch("/user/1/flag"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(anyInt());
        verify(userService, never()).updateUser(any(User.class));
    }

    /**
     * Test retrieving active users when there are some active users.
     * Verifies that only active users are returned.
     */
    @Test
    public void testGetActiveUsers_Success() throws Exception {
        // Create the list of users
        User activeUser1 = new User();
        activeUser1.setId(1);
        activeUser1.setFirstName("John");
        activeUser1.setLastName("Doe");
        activeUser1.setMail("john.doe@example.com");
        activeUser1.setActive(true);

        User activeUser2 = new User();
        activeUser2.setId(2);
        activeUser2.setFirstName("Jane");
        activeUser2.setLastName("Smith");
        activeUser2.setMail("jane.smith@example.com");
        activeUser2.setActive(true);

        User inactiveUser = new User();
        inactiveUser.setId(3);
        inactiveUser.setFirstName("Mike");
        inactiveUser.setLastName("Brown");
        inactiveUser.setMail("mike.brown@example.com");
        inactiveUser.setActive(false);

        List<User> allUsers = List.of(activeUser1, activeUser2, inactiveUser);

        // Mock the service to return all users
        when(userService.getAllUsers()).thenReturn(allUsers);

        // Execute the request and validate the response
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)) // Two active users
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(userService, times(1)).getAllUsers();
    }

    /**
     * Test retrieving active users when there are no active users.
     * Verifies that the response contains no active users.
     */
    @Test
    public void testGetActiveUsers_NoActiveUsers() throws Exception {
        // Create the list of users (no active users)
        User inactiveUser1 = new User();
        inactiveUser1.setId(1);
        inactiveUser1.setFirstName("John");
        inactiveUser1.setLastName("Doe");
        inactiveUser1.setMail("john.doe@example.com");
        inactiveUser1.setActive(false);

        User inactiveUser2 = new User();
        inactiveUser2.setId(2);
        inactiveUser2.setFirstName("Jane");
        inactiveUser2.setLastName("Smith");
        inactiveUser2.setMail("jane.smith@example.com");
        inactiveUser2.setActive(false);

        List<User> allUsers = List.of(inactiveUser1, inactiveUser2);

        // Mock the service to return all users
        when(userService.getAllUsers()).thenReturn(allUsers);

        // Execute the request and validate the response
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)); // No active users

        verify(userService, times(1)).getAllUsers();
    }

    /**
     * Test retrieving active users when the list is empty.
     * Verifies that the response contains no users.
     */
    @Test
    public void testGetActiveUsers_EmptyList() throws Exception {
        // No users in the list
        when(userService.getAllUsers()).thenReturn(List.of());

        // Execute the request and validate the response
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)); // Empty list

        verify(userService, times(1)).getAllUsers();
    }

    /**
     * Test retrieving active users when an internal server error occurs.
     * Verifies that the proper error status is returned.
     */
    @Test
    public void testGetActiveUsers_InternalServerError() {
        // Simulate an exception thrown by the service
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Internal error"));

        // Execute the request and validate the exception
        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(get("/user"))
                    .andExpect(status().isInternalServerError());
        });

        assertEquals("Internal error", exception.getCause().getMessage());

        verify(userService, times(1)).getAllUsers();
    }


}
