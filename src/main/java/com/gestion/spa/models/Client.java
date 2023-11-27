package com.gestion.spa.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "client")
@NoArgsConstructor
@Data
public class Client implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    @Size(min = 3,message = "client name is too small")
    private String name;
    private String randomized;
    @Size(min = 9, message = "Phone number should be at least 9 digits")
    @Nullable
    private String phone;
    @Nullable
    private String address;
    @OneToMany(mappedBy = "client",cascade = CascadeType.DETACH)
    private List<Product> productList;
    @JsonManagedReference(value = "product-client")
    public List<Product> getProductList() {
        return productList;
    }

    @OneToMany(mappedBy = "client",cascade = CascadeType.DETACH)
    private List<Facturation> facturationList;
    @JsonManagedReference(value = "facturation-client")
    public List<Facturation> getFacturationList() {
        return facturationList;
    }
}
