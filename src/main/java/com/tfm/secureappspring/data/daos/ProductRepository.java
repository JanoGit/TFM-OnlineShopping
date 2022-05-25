package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByItem(String item);

}
