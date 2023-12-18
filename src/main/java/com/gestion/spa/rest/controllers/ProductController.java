package com.gestion.spa.rest.controllers;

import com.gestion.spa.models.Product;
import com.gestion.spa.models.UserEntity;
import com.gestion.spa.repositories.ProductRepository;
import com.gestion.spa.repositories.UserRepository;
import com.opencsv.CSVWriter;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.RemoteEndpoint;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    @Autowired
    private ProductController(ProductRepository productRepository, UserRepository userRepository)
    {
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }
    @PostMapping("add")
    public Object addProduct(@RequestBody Product product)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> logged_in_user = userRepository.findByUsername(auth.getName());
        if(logged_in_user.isPresent())
        {
           long id= logged_in_user.get().getId();
           product.setEmployee_id(id);
           product.setDateTime(LocalDate.now());
           productRepository.save(product);
           return product;
        }
        else{
            return "user not logged in";
        }

    }
    @GetMapping("employee/get")
    public Object viewClientProduct()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> logged_in_user = userRepository.findByUsername(auth.getName());
        List<Product> products = productRepository.findAll();
        List<Product> cache = new ArrayList<>();
        if(logged_in_user.isPresent())
        {
            for(Product p : products)
            {
                if(p.getEmployee_id() == logged_in_user.get().getId())
                {
                 cache.add(p);
                }
            }
            return new ResponseEntity<List<Product>>(cache, HttpStatus.OK);
        }
        return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("all")
    public List<Product> findAllProducts() throws IOException , GitAPIException {
       Writer writer = new Writer();
//        CredentialsProvider cp = new UsernamePasswordCredentialsProvider("alphadigitalservices237","Germanin@ictu20");

                 writer.writeToCSV(productRepository.findAll(),"products.csv");
        /*try {
            CredentialsProvider cp = new UsernamePasswordCredentialsProvider("ndjana30","Germanin@ictu20");


            Git g = Git.open(new File("C:/Users/godma/Documents/spa/spa/spa/spa"));
            g.add().addFilepattern(".").call();
            g.commit().setMessage("New Commit").setCredentialsProvider(cp).call();
            g.push().setCredentialsProvider(cp).call();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/


        /*

            FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File("C:/Users/godma/Documents/spa/spa/spa/spa"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
        repository.isBare();


        Git git = new Git(repository);
        git.add().addFilepattern(".").call();
        git.commit().setMessage("New Commit").setCredentialsProvider(cp);
        git.push().setCredentialsProvider(cp).call();*/

        return productRepository.findAll();

    }

}
