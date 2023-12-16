package com.gestion.spa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="product")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private double cost;
    @ManyToOne
    @JoinColumn(name = "employee_id",insertable = false,updatable = false)
    private UserEntity employee;
    @ManyToOne
    @JoinColumn(name = "client_id",insertable = false,updatable = false)
    private Client client;
    private long client_id;
    private long employee_id;
    @Nullable
    private String detail;

    private LocalDate dateTime;

    @JsonBackReference(value = "product-client")
    public Client getClient() {
        return client;
    }

    @JsonBackReference(value = "employee-products")
    public UserEntity getEmployee() {
        return employee;
    }

    @ManyToMany
    private List<Facturation>facturationList;
}
