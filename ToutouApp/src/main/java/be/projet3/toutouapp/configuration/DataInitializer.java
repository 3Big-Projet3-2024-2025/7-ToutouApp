package be.projet3.toutouapp.configuration;

import be.projet3.toutouapp.models.Role;
import be.projet3.toutouapp.models.DogCategory;
import be.projet3.toutouapp.repositories.jpa.RoleRepository;
import be.projet3.toutouapp.repositories.jpa.DogCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component that initializes roles and dog categories in the database at application startup.
 * Implements {@link CommandLineRunner} to execute the initialization logic after the application context is loaded.
 * This ensures that roles and dog categories are pre-loaded into the database if they don't already exist.
 * @author Damien DeLeeuw
 */
@Component
public class DataInitializer implements CommandLineRunner {

    /**
     * Repository for performing CRUD operations on Role entities.
     * Automatically injected by Spring using the {@link Autowired} annotation.
     *
     */
    private final RoleRepository roleRepository;

    /**
     * Repository for performing CRUD operations on DogCategory entities.
     * Automatically injected by Spring using the {@link Autowired} annotation.
     *
     */
    private final DogCategoryRepository dogCategoryRepository;

    /**
     * Constructs a new {@link DataInitializer} with the provided repositories.
     *
     * @param roleRepository         the repository for {@link Role} entities
     * @param dogCategoryRepository  the repository for {@link DogCategory} entities
     * @author Damien DeLeeuw
     */
    public DataInitializer(RoleRepository roleRepository, DogCategoryRepository dogCategoryRepository) {
        this.roleRepository = roleRepository;
        this.dogCategoryRepository = dogCategoryRepository;
    }


    /**
     * Initializes the roles and dog categories in the database if they don't already exist.
     * This method will be called automatically when the application starts.
     *
     * - Creates and saves two roles: "USER" and "ADMIN" if no roles are found in the database.
     * - Creates and saves three dog categories: "Big", "Medium", and "Small" if no categories are found in the database.
     *
     * @param args command-line arguments (not used in this implementation)
     * @throws Exception if an error occurs during the initialization process
     * @author Damien DeLeeuw
     */
    @Override
    public void run(String... args) throws Exception {
        // Initialize roles with specific IDs
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setRoleId(1);
            userRole.setName("USER");

            Role adminRole = new Role();
            adminRole.setRoleId(2);
            adminRole.setName("ADMIN");

            roleRepository.save(userRole);
            roleRepository.save(adminRole);

            System.out.println("Roles initialized with specific IDs.");
        }

        // Initialize dog categories with specific IDs
        if (dogCategoryRepository.count() == 0) {
            DogCategory bigCategory = new DogCategory();
            bigCategory.setDogCategoryId(1);
            bigCategory.setCategory("Big");

            DogCategory mediumCategory = new DogCategory();
            mediumCategory.setDogCategoryId(2);
            mediumCategory.setCategory("Medium");

            DogCategory smallCategory = new DogCategory();
            smallCategory.setDogCategoryId(3);
            smallCategory.setCategory("Small");

            dogCategoryRepository.save(bigCategory);
            dogCategoryRepository.save(mediumCategory);
            dogCategoryRepository.save(smallCategory);

            System.out.println("Dog categories initialized with specific IDs.");
        }
    }
}
