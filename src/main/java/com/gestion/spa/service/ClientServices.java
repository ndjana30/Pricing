package com.gestion.spa.service;

import com.gestion.spa.models.Client;
import com.gestion.spa.models.Product;
import com.gestion.spa.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServices {
    @Autowired
    private ClientRepository clientRepository;

    public List<Product> FindProductsByDate(LocalDate date, long id )
    {

        List<Product> products=new ArrayList<>();
        Optional<Client> client = clientRepository.findById(id);
        if(client.isPresent())
        {
            for(Product product: client.get().getProductList())
            {
                if (product.getDateTime().equals(date))
                {
                    products.add(product);
                }
                return products;
            }
        }

        return null;
    }
}
