package com.tfm.secureappspring.data.models;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "supply_orders", indexes = {
        @Index(name = "fk_supply_orders_products_idx", columnList = "products_id")
})
public class SupplyOrder {
    @EmbeddedId
    private SupplyOrderId id;

    @MapsId("productsId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "products_id", nullable = false)
    private Product products;

}