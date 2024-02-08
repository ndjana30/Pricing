package com.limiter.demo.rest.auth;


import com.limiter.demo.dto.AuthResponseDto;
import com.limiter.demo.dto.LoginDto;
import com.limiter.demo.dto.RegisterDto;
import com.limiter.demo.models.Role;
import com.limiter.demo.models.UserEntity;
import com.limiter.demo.repositories.RoleRepository;
import com.limiter.demo.repositories.UserRepository;
import com.limiter.demo.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("employee/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication usr = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }



@GetMapping("user/get")
public Object getCurrentUser()
{
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Authentication user = SecurityContextHolder.getContext().getAuthentication();
    Role cashier=roleRepository.findByName("CASHIER").get();
    Role employee=roleRepository.findByName("EMPLOYEE").get();
    Role manager=roleRepository.findByName("MANAGER").get();
    Role admin=roleRepository.findByName("ADMIN").get();
    if(user.getAuthorities().toArray()[0].toString().equals(cashier.getName()))
    {
        return cashier.getName();
    }
    else if (user.getAuthorities().toArray()[0].toString().equals(employee.getName()))
    {
        return employee.getName();
    }
    else if(user.getAuthorities().toArray()[0].toString().equals(manager.getName()))
    {
        return manager.getName();
    }

    else if(user.getAuthorities().toArray()[0].toString().equals(admin.getName()))
    {
        return admin.getName();
    }
    return new ResponseEntity<String>("User Not Authenticated",HttpStatus.BAD_REQUEST);


}

    @PostMapping("admin/register")
    public ResponseEntity<String> adminRegister(@RequestBody RegisterDto registerDto)
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role roles= roleRepository.findByName("ADMIN").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with Admin role",
                HttpStatus.OK);
    }
    @PostMapping("client/register")
    public ResponseEntity<String> clientRegister(@RequestBody RegisterDto registerDto)
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles= roleRepository.findByName("CLIENT").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with Client status",
                HttpStatus.OK);
    }
    @PostMapping("waiter/register")
    public ResponseEntity<String> waiterRegister(@RequestBody RegisterDto registerDto)
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles= roleRepository.findByName("WAITER").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with 'waiter' role",
                HttpStatus.OK);
    }

    @PostMapping("cook/register")
    public ResponseEntity<String> cookRegister(@RequestBody RegisterDto registerDto)
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles= roleRepository.findByName("COOK").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with cook role",
                HttpStatus.OK);
    }
    @GetMapping("connected/see")
    public Object seeUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByUsername(authentication.getName());
        if(user.isPresent())
        {
            return user.get();
        }
        return new ResponseEntity<>("User not logged in",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable Long id)
    {
        Optional<UserEntity> user = userRepository.findById(id);
       if (user.isPresent())
       {
           userRepository.deleteById(id);
           return new ResponseEntity<>("User deleted",HttpStatus.OK);
       }
        return new ResponseEntity<>("User not found",HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/employee/{id}/update")
    public ResponseEntity<Object> updateEmployee(@PathVariable long id, @RequestBody UserEntity user)
    {
        
        Optional<UserEntity> user1 = userRepository.findById(id);
        if(user1.isPresent())
        {
            user1.get().setUsername(user.getUsername());
            user1.get().setPassword(user.getPassword());
            userRepository.save(user1.get());
            return new ResponseEntity<>("User modified", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
    }


}
