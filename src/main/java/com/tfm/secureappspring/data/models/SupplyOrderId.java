package com.tfm.secureappspring.data.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class SupplyOrderId implements Serializable {
    private static final long serialVersionUID = 5191013200367644412L;
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "products_id", nullable = false)
    private Integer productsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SupplyOrderId entity = (SupplyOrderId) o;
        return Objects.equals(this.productsId, entity.productsId) &&
                Objects.equals(this.id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productsId, id);
    }

}