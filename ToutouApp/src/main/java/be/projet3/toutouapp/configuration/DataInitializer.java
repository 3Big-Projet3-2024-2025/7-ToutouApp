package be.projet3.toutouapp.configuration;

import be.projet3.toutouapp.models.Role;
import be.projet3.toutouapp.models.DogCategory;
import be.projet3.toutouapp.repositories.jpa.RoleRepository;
import be.projet3.toutouapp.repositories.jpa.DogCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final DogCategoryRepository dogCategoryRepository;

    public DataInitializer(RoleRepository roleRepository, DogCategoryRepository dogCategoryRepository) {
        this.roleRepository = roleRepository;
        this.dogCategoryRepository = dogCategoryRepository;
    }

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
