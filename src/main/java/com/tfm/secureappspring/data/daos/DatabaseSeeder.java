package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Product;
import com.tfm.secureappspring.data.models.Role;
import com.tfm.secureappspring.data.models.User;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public class DatabaseSeeder {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private static final String MAIL = "admin@admin.com";
    private static final String PASSWORD = "admin";
    private static final String USERNAME = "admin";

    @Autowired
    public DatabaseSeeder(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.deleteAllAndInitializeAndSeedDatabase();
    }

    public  void deleteAllAndInitializeAndSeedDatabase() {
        this.deleteAllAndInitialize();
        this.initialize();
        this.seedDatabase();
    }

    private void deleteAllAndInitialize() {
        this.userRepository.deleteAll();
        this.productRepository.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Deleted All -----------");
    }

    void initialize() {
        LogManager.getLogger(this.getClass()).warn("------- Finding Admin -----------");
        if (this.userRepository.findByRoleIn(List.of(Role.ADMIN)).isEmpty()) {
            User user = User.builder().id(1).mail(MAIL).password(PASSWORD) // new BCryptPasswordEncoder().encode(PASSWORD)
                    .role(Role.ADMIN).userName(USERNAME).enabled(Boolean.TRUE).registrationDate(LocalDateTime.now()).build();
            this.userRepository.save(user);
            LogManager.getLogger(this.getClass()).warn("------- Created Admin -----------");
        }
    }

    private void seedDatabase() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        Product[] products = {
                Product.builder().id(1).name("Iphone").amount(50).price(840.35).build(),
                Product.builder().id(2).name("Android").amount(25).price(532.85).build()
        };
        this.productRepository.saveAll(Arrays.asList(products));
        LogManager.getLogger(this.getClass()).warn("        ------- products");
    }
}
