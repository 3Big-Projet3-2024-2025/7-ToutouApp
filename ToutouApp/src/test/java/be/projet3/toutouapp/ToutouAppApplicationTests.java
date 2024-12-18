package be.projet3.toutouapp;

import be.projet3.toutouapp.models.User;
import be.projet3.toutouapp.repositories.jpa.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ToutouAppApplicationTests {

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

    @Test
    @Order(1)
    void testCreateUserFromToken() {
        // Obtain a valid JWT token
        jwt = obtainAccessToken();

        // Call the API to create a user
        given()
                .auth().oauth2(jwt)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("http://localhost:8080/user/create")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void testGetAllEmails() {

        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalStateException("JWT is not initialized. Ensure testCreateUserFromToken is executed first.");
        }

        // Count the total number of users in the database
        long totalUsersInDB = userRepository.count();
        System.out.println("Total users in DB: " + totalUsersInDB);

        // Call the API to get the total number of emails
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

        // Ensure the number of users in DB matches the number of emails from API
        assert totalUsersInDB == totalEmailsFromAPI
                : "Mismatch: Total users in DB (" + totalUsersInDB + ") is not equal to total emails from API (" + totalEmailsFromAPI + ").";

        System.out.println("Test passed: Total users in DB matches total emails from API.");
    }

    @Test
    @Order(3)
    void testDuplicateUserCreation() {
        // Ensure the JWT token is initialized
        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalStateException("JWT is not initialized. Ensure testCreateUserFromToken is executed first.");
        }

        // Attempt to create the same user again
        given()
                .auth().oauth2(jwt)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("http://localhost:8080/user/create")
                .then()
                .statusCode(500); // Expecting HTTP 500 Internal Server Error for duplicate creation
    }
    
    @Test
    @Order(4)
    void testUnauthorizedAccess() {
        // Call the API without authentication
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:8080/user/emails")
                .then()
                .statusCode(401); // Ensure unauthorized access is denied
    }

    @Test
    @Order(5)
    void cleanupTestUser() {
        // Cleanup: Remove the test user from the database at the end
        User user = userRepository.findByMail("test@test.com");
        if (user != null) {
            userRepository.deleteById(user.getId());
            System.out.println("Test user deleted successfully.");
        } else {
            System.out.println("Test user does not exist in the database.");
        }
    }
}
