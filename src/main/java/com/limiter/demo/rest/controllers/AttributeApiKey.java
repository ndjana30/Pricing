package com.limiter.demo.rest.controllers;

import com.limiter.demo.models.UserEntity;
import com.limiter.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;

@RequestMapping("api/v1/attribute")
@RestController
public class AttributeApiKey {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("key/basic")
    public Object attributeBasicKey()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(authentication.getName());
        String basic_prefix = "BX001-";
        String generated = new Random().ints(97,122+1)
                .limit(20).toString();
        String api_key = basic_prefix+generated;

        if(user.isPresent())
        {
            user.get().setApiKey(api_key);
            userRepository.save(user.get());
            return new ResponseEntity<>("API KEY BOUGHT for" +user.get().getUsername(), HttpStatus.OK);

        }
        return new ResponseEntity<>("You need to login", HttpStatus.BAD_REQUEST);
    }
}
