package com.gestion.spa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Table(name="Item")
@Entity
@Data
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue
    private long id;
    @Size(min = 3,message = "item name is too small")
    private String name;
    @Enumerated(EnumType.STRING)
    @Nullable
    private Category category;
    @Nullable
    private Integer minQty;
    @Nullable
    private Integer quantity;
    @Nullable
    private LocalDate expiryDate;
    @Nullable
    private String brand;
    @ManyToOne
    @JoinColumn(name = "stock_id",insertable = false,updatable = false)
    private Stock stock;
    private long stock_id;

    public Item(String name, String brand, long stock_id, int minQty, LocalDate expiryDate, int quantity) {
        this.name = name;
        this.quantity=quantity;
        this.brand = brand;
        this.stock_id = stock_id;
        this.minQty=minQty;
        this.expiryDate=expiryDate;
    }

    @JsonBackReference(value = "stock-item")
    public Stock getStock() {
        return stock;
    }

}
