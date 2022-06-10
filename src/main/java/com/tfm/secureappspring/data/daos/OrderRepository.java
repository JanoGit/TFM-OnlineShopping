package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = "SELECT * FROM tfmdb.order", nativeQuery = true)
    List<Order> getAll();
}