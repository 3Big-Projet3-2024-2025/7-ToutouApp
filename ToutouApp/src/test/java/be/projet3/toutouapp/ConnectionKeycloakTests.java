package be.projet3.toutouapp;

import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for validating Keycloak authentication and API functionalities.
 * These tests verify JWT token retrieval, user creation, email retrieval, duplicate handling, and authorization.
 *
 * @see be.projet3.toutouapp
 * @author Damien De Leeuw
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConnectionKeycloakTests {

    @Autowired
    private UserRepository userRepository;

    private static String jwt;

    /*
     * IMPORTANT: Before running these tests, ensure the following user exists in Keycloak:
     *
     * First name: Test
     * Last name: SuperTest
     * Username: test@test.com
     * Email: test@test.com
     * Country: Belgique
     * City: Test
     * Street: Rue du test 50
     * Postal code: 1000
     * Password: test
     *
     * This user is required for authentication and test validations.
     */

    /**
     * Retrieves a valid JWT token from Keycloak.
     * This token is used to authenticate requests to the API.
     *
     * @return the JWT token as a {@code String}.
     */
    private String obtainAccessToken() {
        return given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("client_id", "backend")
                .formParam("client_secret", "RSQRkZjjWjGqMsTPJd9RCy232aYOHr7r")
                .formParam("username", "test@test.com")
                .formParam("password", "test")
                .formParam("grant_type", "password")
                .post("http://localhost:8081/realms/HappyPaws/protocol/openid-connect/token")
                .then()
                .statusCode(200)
                .extract()
                .path("access_token"); // Extract and return the access token
    }

    /**
     * Tests user creation using a valid JWT token.
     */
    @Test
    @Order(1)
    @DisplayName("Create a user using a valid JWT token")
    void testCreateUserFromToken() {
        jwt = obtainAccessToken();

        given()
                .auth().oauth2(jwt)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("http://localhost:8080/user/create")
                .then()
                .statusCode(200);
    }

    /**
     * Validates that the total number of emails retrieved via API matches the user count in the database.
     */
    @Test
    @Order(2)
    @DisplayName("Retrieve all emails and validate count matches users in database")
    void testGetAllEmails() {
        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalStateException("JWT is not initialized. Ensure testCreateUserFromToken is executed first.");
        }

        long totalUsersInDB = userRepository.count();
        System.out.println("Total users in DB: " + totalUsersInDB);

        int totalEmailsFromAPI = given()
                .auth().oauth2(jwt)
                .when()
                .get("http://localhost:8080/user/emails")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract()
                .path("size()");

        System.out.println("Total emails from API: " + totalEmailsFromAPI);

        assert totalUsersInDB == totalEmailsFromAPI
                : "Mismatch: Total users in DB (" + totalUsersInDB + ") is not equal to total emails from API (" + totalEmailsFromAPI + ").";

        System.out.println("Test passed: Total users in DB matches total emails from API.");
    }

    /**
     * Tests that attempting to create a duplicate user results in an error.
     */
    @Test
    @Order(3)
    @DisplayName("Attempt to create a duplicate user and expect failure")
    void testDuplicateUserCreation() {
        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalStateException("JWT is not initialized. Ensure testCreateUserFromToken is executed first.");
        }

        given()
                .auth().oauth2(jwt)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("http://localhost:8080/user/create")
                .then()
                .statusCode(500);
    }

    /**
     * Verifies that unauthorized API access is denied when no authentication token is provided.
     */
    @Test
    @Order(4)
    @DisplayName("Verify unauthorized access is denied without authentication")
    void testUnauthorizedAccess() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:8080/user/emails")
                .then()
                .statusCode(401);
    }

    /**
     * Cleans up by deleting the test user from the database.
     */
    @Test
    @Order(5)
    @DisplayName("Clean up: Delete the test user from the database")
    void cleanupTestUser() {
        User user = userRepository.findByMail("test@test.com");
        if (user != null) {
            userRepository.deleteById(user.getId());
            System.out.println("Test user deleted successfully.");
        } else {
            System.out.println("Test user does not exist in the database.");
        }
    }
}
