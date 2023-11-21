package com.gestion.spa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

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
    private Category category;

    private String brand;
    @ManyToOne
    @JoinColumn(name = "stock_id",insertable = false,updatable = false)
    private Stock stock;
    private long stock_id;

    public Item(String name, Category category, String brand, long stock_id) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.stock_id = stock_id;
    }

    @JsonBackReference(value = "stock-item")
    public Stock getStock() {
        return stock;
    }

}
