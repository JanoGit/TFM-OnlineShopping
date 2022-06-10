package com.tfm.secureappspring.data.models;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "purchased_product", indexes = {
        @Index(name = "fk_purchased_product_products_idx", columnList = "product_id"),
        @Index(name = "fk_purchased_product_order_idx", columnList = "order_id")
})
public class PurchasedProduct {
    @EmbeddedId
    private PurchasedProductId id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

}