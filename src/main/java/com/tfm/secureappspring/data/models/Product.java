package com.tfm.secureappspring.data.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
//@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotBlank(message = "Product name has to be set")
    private String name;
    @NotNull(message = "Amount has to be equal or greater than zero")
    @PositiveOrZero(message = "Amount has to be equal or greater than zero")
    private Integer amount;
    @NotNull(message = "Price has to be equal or greater than zero")
    @PositiveOrZero(message = "Price has to be equal or greater than zero")
    private Double price;

    @OneToMany(mappedBy = "product")
    private Set<PurchasedProduct> purchasedProducts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "products")
    private Set<SupplyOrder> supplyOrders = new LinkedHashSet<>();

}
