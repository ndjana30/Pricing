package com.limiter.demo.api;

import com.fasterxml.jackson.databind.ObjectReader;
import com.limiter.demo.models.UserEntity;
import com.limiter.demo.repositories.UserRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class Mult {

//    private final Bucket bucket;
    @Autowired
    private PricingPlanService pricingPlanService;
    @Autowired
    private UserRepository userRepository;
    /*public Mult()
    {
        *//*Bandwidth limit = Bandwidth.classic(1, Refill.greedy(1, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();*//*

        //Here, every Api in this controller is allowed to produce one result per minute
    }*/


    @GetMapping("multiply/{a}/{b}")
    public ResponseEntity<Object> multiply(@RequestHeader(value = "X-api-key") String apiKey, @PathVariable int a, @PathVariable int b)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(auth.getName());
        if(user.isPresent())
        {
            if(apiKey == null || apiKey.isEmpty() )
            {
                apiKey=user.get().getApiKey();
                Bucket bucket = pricingPlanService.resolveBucket(apiKey);
                ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
                if (probe.isConsumed())
                {
                    return ResponseEntity.ok()
                            .header("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()))
                            .body(a*b);
                }
                return new ResponseEntity<>("Too many requests, upgrade to send more requests per hour", HttpStatus.TOO_MANY_REQUESTS);

            }
            else{

                Bucket bucket = pricingPlanService.resolveBucket(apiKey);
                ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
                if (probe.isConsumed())
                {
                    return ResponseEntity.ok()
                            .header("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()))
                            .body(a*b);
                }
                return new ResponseEntity<>("Too many requests, upgrade to send more requests per hour", HttpStatus.TOO_MANY_REQUESTS);

            }
        }
        return new ResponseEntity<>("user needs to login", HttpStatus.TOO_MANY_REQUESTS);


    }



}
