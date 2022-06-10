package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.PurchasedProduct;
import com.tfm.secureappspring.data.models.PurchasedProductId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, PurchasedProductId> {
}