package com.tfm.secureappspring.data.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

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
    private String item;
    @PositiveOrZero(message = "Amount has to be equal or greater than zero")
    @Column(nullable = false)
    private double amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Double.compare(product.amount, amount) == 0 && item.equals(product.item);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
