package be.projet3.toutouapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class ToutouAppApplicationTests {


    @Test
    void contextLoads() {
    }
    @Test
    void testOpenApiDocs() {
        given()
                .when()
                .get("http://localhost:8080/v3/api-docs")
                .then()
                .statusCode(200)
                .body("openapi", equalTo("3.0.1"));
    }
}
