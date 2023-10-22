package com.gestion.spa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="facturation")
@Data
@NoArgsConstructor
public class Facturation {
    @Id
    @GeneratedValue
    private long id;

    private LocalDate dateTime;
    @ManyToOne
    @JoinColumn(name = "client_id",insertable = false,updatable = false)
    private Client client;
    private long client_id;
    private Double total;
    @JsonBackReference(value = "facturation-client")
    public Client getClient() {
        return client;
    }

    //@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    //@JoinTable(name = "facturation_product", joinColumns = @JoinColumn(name="facturation_id", referencedColumnName = "id"),
          //  inverseJoinColumns = @JoinColumn(name="product_id", referencedColumnName = "id"))
    //private List<Product> products=  new ArrayList<>();
    @ManyToMany
    private List<Product> products = new ArrayList<>();
}
