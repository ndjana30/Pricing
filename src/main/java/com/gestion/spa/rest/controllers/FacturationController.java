package com.gestion.spa.rest.controllers;

import com.gestion.spa.models.Client;
import com.gestion.spa.models.Facturation;
import com.gestion.spa.models.Product;
import com.gestion.spa.models.UserEntity;
import com.gestion.spa.repositories.ClientRepository;
import com.gestion.spa.repositories.FacturationRepository;
import com.gestion.spa.repositories.UserRepository;
import com.gestion.spa.service.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/facturation")
public class FacturationController {
    private FacturationRepository facturationRepository;
    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private ClientServices clientServices;
    @Autowired
    private FacturationController(FacturationRepository facturationRepository,
                                  UserRepository userRepository,
                                  ClientRepository clientRepository,
                                  ClientServices clientServices)
    {
        this.facturationRepository=facturationRepository;
        this.userRepository=userRepository;
        this.clientRepository=clientRepository;
        this.clientServices=clientServices;
    }

    @GetMapping("{client_id}/see")
    public Facturation addFacturation(
                                      @PathVariable long client_id)
    {
        Facturation facturation=new Facturation();
        List<Double> costs = new ArrayList<>();
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> employee = userRepository.findByUsername(auth.getName());
        Optional<Client> client = clientRepository.findById(client_id);

        if(employee.isPresent())
        {
            if(client.isPresent()) {
                for (Product p : client.get().getProductList()) {
                    costs.add(p.getCost());
                   facturation.getProducts().add(p);
                }
                Double sum = costs.stream().reduce(0.0, Double::sum);
                facturation.setTotal(sum);
                facturation.setDateTime(LocalDate.now());
                    facturation.setClient_id(client.get().getId());
                    facturationRepository.save(facturation);
                    return facturation;
            }
        }
        return null;
    }
    /*
    @GetMapping("{client_id}/see/{date}")
    public List<Facturation> SeeFacturationPerDate(
            @PathVariable long client_id, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date)
    {
        Optional<Client> client = clientRepository.findById(client_id);
        if(client.isPresent())
        {
            for(Facturation fac:client.get().getFacturationList())
            {
                if(fac.getDateTime().isEqual(date))
                {
                        return fac;
                }
                else {
                    return null;
                }
            }
        }
        return null;
    }

     */

    @GetMapping("{client_id}/see/{date}")
    public Facturation SeeFacturationPerDate(
            @PathVariable long client_id, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date)
    {
        Client client = clientRepository.findById(client_id).get();
        Facturation facturation = new Facturation();
        List<Double> costs = new ArrayList<>();
         for(Product p: client.getProductList())
         {
             if (p.getDateTime().equals(date))
             {
                 facturation.getProducts().add(p);
                 costs.add(p.getCost());
             }

         }
        Double sum = costs.stream().reduce(0.0, Double::sum);
         facturation.setTotal(sum);
         facturation.setClient_id(client_id);
          return facturation;

    }
}
