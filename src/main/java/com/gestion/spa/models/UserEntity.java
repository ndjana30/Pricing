package com.gestion.spa.models;


import javax.persistence.*;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jdk.jfr.BooleanFlag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    @Size(min = 3,message = "username name is too small")
    private String username;
    @Column(nullable = false)
    private String password;
    private String NIC;
    private double salary;
    private Date dateOfBirth;
    @Nullable
    private String randomized;


    public void setId(long id) {
        this.id = id;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setNIC(String NIC) {
        this.NIC = NIC;
    }


    public void setSalary(double salary) {
        this.salary = salary;
    }


    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Nullable
    public String getRandomized() {
        return randomized;
    }

    public void setRandomized(@Nullable String randomized) {
        this.randomized = randomized;
    }


    public void setActive(Boolean active) {
        this.active = active;
    }

    @OneToMany(cascade = CascadeType.DETACH,mappedBy = "employee" )
    private List<Product> products;
    @JsonManagedReference(value = "employee-products")
    public List<Product> getProducts() {
        return products;
    }

    private Boolean active=true;

    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "id"))
    private List<Role> roles =  new ArrayList<>();


    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}

