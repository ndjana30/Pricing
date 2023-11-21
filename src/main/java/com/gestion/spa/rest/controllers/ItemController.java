package com.gestion.spa.rest.controllers;

import com.gestion.spa.models.Category;
import com.gestion.spa.models.Item;
import com.gestion.spa.models.Stock;
import com.gestion.spa.repositories.ItemRepository;
import com.gestion.spa.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/stock")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StockRepository stockRepository;

    /*@PostMapping("{s_id}/items/{name}/remove")
    public ResponseEntity<String> deleteItem(@PathVariable long s_id, @RequestParam int number, @PathVariable String name) {
        Optional<Stock> stock = stockRepository.findById(s_id);
        if (stock.isPresent()) {
            List<Item> items = stock.get().getItemsList();
            Optional<Item> nameItem = itemRepository.findByName(name);

            for (int i = 0; i < number; i++) {
                for (Item it : items) {
                    if (it.getName().equals(nameItem.get().getName())) {
                        itemRepository.delete(it);
                    }

                }
            }

        }
        return new ResponseEntity<>("Item(s) Deleted", HttpStatus.OK);
    }*/

@PostMapping("{s_id}/items/add/{number}")
    public ResponseEntity<String> addItems(@PathVariable long s_id, @PathVariable int number
        , @RequestParam String name, @RequestParam String brand, @RequestParam Category category)
{
    Optional<Stock> stock = stockRepository.findById(s_id);
    if (stock.isPresent())
    {

        for (int i = 0; i < number; i++)
        {
            Item item = new Item(name, category, brand, s_id);
            itemRepository.save(item);
        }
        return new ResponseEntity<>("Item(s) Added",HttpStatus.OK);
    }
    return new ResponseEntity<>("Stock is not present",HttpStatus.BAD_REQUEST);
}


    @GetMapping("{s_id}/items/all/")
    public ResponseEntity<List<Item>> seeItems(@PathVariable long s_id)
    {
        Optional<Stock> stock = stockRepository.findById(s_id);
        if (stock.isPresent())
        {
            return new ResponseEntity<>(stock.get().getItemsList(), HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("{s_id}/items/{number}/delete")
    public ResponseEntity<String> deleteItems(@PathVariable long s_id, @PathVariable int number, @RequestParam String name)
    {
        Optional<Stock> stock = stockRepository.findById(s_id);
        List<Item> items = new ArrayList<>();
        if (stock.isPresent())
        {

                for(Item item: stock.get().getItemsList())
                {
                    if(item.getName().equals(name))
                        {
                            items.add(item);
                        }
                    }
                for (int i = 0; i<number ; i++)
                {
                    Item it = items.get(i);
                    itemRepository.delete(it);
                }

            return new ResponseEntity<>("Items deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("Stock not Found",HttpStatus.BAD_REQUEST);

    }


}
