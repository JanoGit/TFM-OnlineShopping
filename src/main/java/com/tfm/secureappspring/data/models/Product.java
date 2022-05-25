package com.tfm.secureappspring.data.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotBlank(message = "Product name has to be set")
    private String name;
    @PositiveOrZero(message = "Amount has to be equal or greater than zero")
    @Column(nullable = false)
    private double amount;
    @Column(name = "price", nullable = false)
    private Double price;

    @OneToMany(mappedBy = "products")
    @ToString.Exclude
    private Set<PurchasedProduct> purchasedProducts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "products")
    @ToString.Exclude
    private Set<SupplyOrder> supplyOrders = new LinkedHashSet<>();

}
