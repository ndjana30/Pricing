package com.gestion.spa.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Table(name = "Stock")
@Entity
@Data
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue
    private long id;
    @Size(min = 3,message = "stock name is too small")
    private String name;
    @OneToMany(mappedBy = "stock",cascade = CascadeType.DETACH)
    private List<Item> itemsList;
    @JsonManagedReference(value = "stock-item")
    public List<Item> getItemsList() {
        return itemsList;
    }

}
