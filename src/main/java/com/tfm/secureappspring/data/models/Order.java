package com.tfm.secureappspring.data.models;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "order", indexes = {
        @Index(name = "fk_order_users_idx", columnList = "users_id")
})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "cost", nullable = false)
    private Double cost;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "users_id", nullable = false)
    @ToString.Exclude
    private User users;

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private Set<PurchasedProduct> purchasedProducts = new LinkedHashSet<>();

}