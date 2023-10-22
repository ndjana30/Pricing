package com.gestion.spa.rest.controllers;

import com.gestion.spa.models.Client;
import com.gestion.spa.models.Role;
import com.gestion.spa.models.UserEntity;
import com.gestion.spa.repositories.ClientRepository;
import com.gestion.spa.repositories.RoleRepository;
import com.gestion.spa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("api/v1/client")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @PostMapping("add")
    public Client addClient(@RequestBody Client client)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> employee = userRepository.findByUsername(auth.getName());
        Optional<Role> role = roleRepository.findByName("CASHIER");
        for(Role r : employee.get().getRoles())
        {
            if(r.getName().equals(role.get().getName()))
            {
                client.setRandomized(new Random().toString());
                clientRepository.save(client);
                return client;
            }
            break;
        }

        return null;
    }

}
