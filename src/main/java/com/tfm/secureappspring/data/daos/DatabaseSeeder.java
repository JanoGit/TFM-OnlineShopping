package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Product;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public class DatabaseSeeder {

    private final DatabaseStarting databaseStarting;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DatabaseSeeder(DatabaseStarting databaseStarting, UserRepository userRepository,
                          ProductRepository productRepository) {
        this.databaseStarting = databaseStarting;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.deleteAllAndInitializeAndSeedDatabase();
    }

    public  void deleteAllAndInitializeAndSeedDatabase() {
        this.deleteAllAndInitialize();
        this.seedDatabase();
    }

    private void deleteAllAndInitialize() {
        this.userRepository.deleteAll();
        this.productRepository.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Deleted All -----------");
        this.databaseStarting.initialize();
    }

    private void seedDatabase() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        Product[] products = {
                Product.builder().name("Iphone").amount(50).price(840.35).build(),
                Product.builder().name("Android").amount(25).price(532.85).build()
        };
        this.productRepository.saveAll(Arrays.asList(products));
        LogManager.getLogger(this.getClass()).warn("        ------- products");
    }
}
