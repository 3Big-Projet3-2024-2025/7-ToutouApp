package be.projet3.toutouapp.controllers;


import be.projet3.toutouapp.services.DatabaseTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseTestController {

    @Autowired
    private DatabaseTestService databaseTestService;

    @GetMapping("/test-db")
    public String testDatabaseConnection() {
        return databaseTestService.testDatabaseConnection();
    }
}
