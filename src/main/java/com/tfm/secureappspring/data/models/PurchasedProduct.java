package com.tfm.secureappspring.data.models;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "purchased_product", indexes = {
        @Index(name = "fk_purchased_product_products_idx", columnList = "products_id"),
        @Index(name = "fk_purchased_product_order_idx", columnList = "order_id")
})
public class PurchasedProduct {
    @EmbeddedId
    private PurchasedProductId id;

    @MapsId("productsId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "products_id", nullable = false)
    @ToString.Exclude
    private Product products;

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private Order order;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

}