package com.gestion.spa.rest.controllers;

import com.gestion.spa.models.*;
import com.gestion.spa.repositories.ItemRepository;
import com.gestion.spa.repositories.RoleRepository;
import com.gestion.spa.repositories.StockRepository;
import com.gestion.spa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/stock")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

@PostMapping("{s_id}/items/add/{number}")
    public ResponseEntity<String> addItems(@PathVariable long s_id, @PathVariable int number
        , @RequestParam String name, @RequestParam String brand,
                                            @RequestParam int minQty, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expiryDate) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Optional<UserEntity> user = userRepository.findByUsername(auth.getName());

    Optional<Role> role = roleRepository.findByName("MANAGER");
    Optional<Role> admin_role = roleRepository.findByName("ADMIN");
    for (Role r : user.get().getRoles()) {
        if (r.getName().equals(role.get().getName()) || r.getName().equals(admin_role.get().getName()))
        {
            Optional<Stock> stock = stockRepository.findById(s_id);
            if (stock.isPresent()) {
//                https://wellnessspa237.onrender.com/api/v1/stock/{s_id}/items/add/{number}

                    Item item = new Item(name, brand, s_id,minQty,expiryDate,number);
                    itemRepository.save(item);

                return new ResponseEntity<>("Item Added", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Stock is not present", HttpStatus.BAD_REQUEST);
            }
        }

    }
    return null;
}

    @GetMapping("{s_id}/items/all")
    public ResponseEntity<List<Item>> seeItems(@PathVariable long s_id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(auth.getName());
        Optional<Role> role = roleRepository.findByName("MANAGER");
        Optional<Role> admin_role = roleRepository.findByName("ADMIN");
        for (Role r : user.get().getRoles()) {
            if (r.getName().equals(role.get().getName()) || r.getName().equals(admin_role.get().getName())) {
                Optional<Stock> stock = stockRepository.findById(s_id);
                if (stock.isPresent()) {
                    return new ResponseEntity<>(stock.get().getItemsList(), HttpStatus.OK);
                }
                return null;
            }
        }
        return null;

    }

    @GetMapping("{s_id}/group_item/all")
    public Object seeGroupItems(@PathVariable long s_id, @RequestParam String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(auth.getName());
        Optional<Role> role = roleRepository.findByName("MANAGER");
        Optional<Role> admin_role = roleRepository.findByName("ADMIN");
        List<Item> itemList = new ArrayList<>();
        for (Role r : user.get().getRoles()) {
            if (r.getName().equals(role.get().getName()) || r.getName().equals(admin_role.get().getName()))
            {
                Optional<Stock> stock = stockRepository.findById(s_id);
                if (stock.isPresent()) {
//                    return new ResponseEntity<>(stock.get().getItemsList(), HttpStatus.OK);
                    for(Item item : stock.get().getItemsList()) {
                        if ((item.getName().equals(name))) {
                            itemList.add(item);
                        }
                    }
                    return itemList;
                }
                return new ResponseEntity<>("Item with name"+name+"Not found", HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    @PostMapping("{s_id}/items/{i_id}/{number}/delete")
    public ResponseEntity<String> deleteItems(@PathVariable long s_id, @PathVariable Integer number, @PathVariable long i_id)

    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(auth.getName());
        Optional<Role> role = roleRepository.findByName("MANAGER");
        Optional<Role> admin_role = roleRepository.findByName("ADMIN");
        for (Role r : user.get().getRoles())
        {
            if (r.getName().equals(role.get().getName()) || r.getName().equals(admin_role.get().getName()))
            {
                Optional<Stock> stock = stockRepository.findById(s_id);
                List<Item> items = new ArrayList<>();
                if (stock.isPresent())
                {
                    for(Item item:stock.get().getItemsList())
                    {
                        if(item.getId() == i_id)
                        {
                            Integer qty = item.getQuantity();
                            item.setQuantity(qty-number);
                            itemRepository.save(item);
                            return new ResponseEntity<>("Items deleted", HttpStatus.OK);
                        }
                    }
                }
                return new ResponseEntity<>("Stock not present",HttpStatus.BAD_REQUEST);
                }
            return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
                }
        return null;
    }

    @PostMapping("{s_id}/items/{i_id}/{number}/increase")
    public ResponseEntity<String> increaseItems(@PathVariable long s_id, @PathVariable Integer number, @PathVariable long i_id)

    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(auth.getName());
        Optional<Role> role = roleRepository.findByName("MANAGER");
        Optional<Role> admin_role = roleRepository.findByName("ADMIN");
        for (Role r : user.get().getRoles())
        {
            if (r.getName().equals(role.get().getName()) || r.getName().equals(admin_role.get().getName()))
            {
                Optional<Stock> stock = stockRepository.findById(s_id);
                List<Item> items = new ArrayList<>();
                if (stock.isPresent())
                {
                    for(Item item:stock.get().getItemsList())
                    {
                        if(item.getId() == i_id)
                        {
                            Integer qty = item.getQuantity();
                            item.setQuantity(qty+number);
                            itemRepository.save(item);
                            return new ResponseEntity<>("Items increased", HttpStatus.OK);
                        }
                    }
                }
                return new ResponseEntity<>("Stock not present",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

}
