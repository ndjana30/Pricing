package com.gestion.spa.service;

import com.gestion.spa.models.Item;
import com.gestion.spa.repositories.ItemRepository;
import com.gestion.spa.repositories.StockRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Optional;

@Service
public class StockServices {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ItemRepository itemRepository;

    public ResponseEntity<String> exportItem(int number, String name)
    {
        Optional<Item> item = itemRepository.findByName(name);
        if(item.isPresent())
        {
            List<Item> items = itemRepository.findAll();
            for(int i=0;i<number;i++)
            {
                itemRepository.delete(items.get(items.size()-1));
            }
            return new ResponseEntity<>("item(s) deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("No such item present, sorry",HttpStatus.BAD_REQUEST);
    }
}
