package com.tfm.secureappspring.data.daos;

import com.tfm.secureappspring.data.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = "SELECT * FROM tfmdb.order", nativeQuery = true)
    List<Order> getAll();

    @Query(value = "SELECT * FROM tfmdb.order WHERE id = ?", nativeQuery = true)
    Order getOneById(Integer id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tfmdb.order (id, cost, user_id) VALUES (?, ?, ?)", nativeQuery = true)
    void saveEntity(Integer id, Double cost, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tfmdb.order SET cost = ?1 WHERE id = ?2", nativeQuery = true)
    void updateCost(Double cost, Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tfmdb.order", nativeQuery = true)
    void eraseAll();
}