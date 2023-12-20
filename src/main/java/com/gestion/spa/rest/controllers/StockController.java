package com.gestion.spa.rest.controllers;

import com.gestion.spa.models.Item;
import com.gestion.spa.models.Role;
import com.gestion.spa.models.Stock;
import com.gestion.spa.models.UserEntity;
import com.gestion.spa.repositories.RoleRepository;
import com.gestion.spa.repositories.StockRepository;
import com.gestion.spa.repositories.UserRepository;
import com.gestion.spa.service.StockServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/stock")
public class StockController {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @PostMapping("add")
    public ResponseEntity<String> AddStock(@RequestParam String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(auth.getName());
        Optional<Role> admin_role = roleRepository.findByName("ADMIN");
        Optional<Role> role = roleRepository.findByName("MANAGER");
        for (Role r : user.get().getRoles()) {
            if (r.getName().equals(role.get().getName()) || r.getName().equals(admin_role.get().getName())) {
                Stock stock = new Stock();
                stock.setName(name);
                stockRepository.save(stock);
                return new ResponseEntity<>("Stock Added", HttpStatus.OK);
            }
        }
        return null;
    }

    @GetMapping("all")
    public ResponseEntity<List<Stock>> seeStocks()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(auth.getName());
        Optional<Role> role = roleRepository.findByName("MANAGER");
        Optional<Role> admin_role = roleRepository.findByName("ADMIN");
        for(Role r : user.get().getRoles())
        {
            if(r.getName().equals(role.get().getName()) || r.getName().equals(admin_role.get().getName()))
            {
                return new ResponseEntity<>(stockRepository.findAll(), HttpStatus.OK);
            }
        }
            return null;

    }

    @GetMapping("{id}")
    public ResponseEntity<Stock> getStock( @PathVariable long id)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(auth.getName());
        Optional<Role> role = roleRepository.findByName("MANAGER");
        Optional<Role> admin_role = roleRepository.findByName("ADMIN");
        Optional<Stock> stock = stockRepository.findById(id);
        for(Role r : user.get().getRoles())
        {
            if(r.getName().equals(role.get().getName()) || r.getName().equals(admin_role.get().getName()))
            {
                if(stock.isPresent())
                {
                    return new ResponseEntity<>(stock.get(),HttpStatus.OK);
                }
            }
        }
        return null;

    }




}
