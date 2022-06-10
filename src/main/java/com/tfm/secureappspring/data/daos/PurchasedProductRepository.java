package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.PurchasedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, Integer> {
}