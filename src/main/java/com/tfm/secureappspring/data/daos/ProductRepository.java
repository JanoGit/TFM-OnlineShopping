package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
