package com.gestion.spa.rest.auth;


import com.gestion.spa.dto.AuthResponseDto;
import com.gestion.spa.dto.LoginDto;
import com.gestion.spa.dto.RegisterDto;
import com.gestion.spa.models.Client;
import com.gestion.spa.models.Role;
import com.gestion.spa.models.UserEntity;
import com.gestion.spa.repositories.ClientRepository;
import com.gestion.spa.repositories.RoleRepository;
import com.gestion.spa.repositories.UserRepository;
import com.gestion.spa.rest.controllers.UserWriter;
import com.gestion.spa.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;
    private ClientRepository clientRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator,ClientRepository clientRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.clientRepository = clientRepository;
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

@PostMapping("cashier/login")
public ResponseEntity<AuthResponseDto> chashierLogin(@RequestBody LoginDto loginDto)
{
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken
                    (loginDto.getUsername(), loginDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    Authentication usr = SecurityContextHolder.getContext().getAuthentication();
    Role role=roleRepository.findByName("CASHIER").get();
    if(usr.getAuthorities().toArray()[0].toString() .equals(role.getName()))
    {
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }
    return null;

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
    @GetMapping("users/all")
    public ResponseEntity<String> getAllUsers() throws IOException {
        List<UserEntity> users = userRepository.findAll();
        UserWriter writer=new UserWriter();
        writer.ExportUsers(users,"users.csv");
        return new ResponseEntity<>("ALL USERS FOUND AND ADDED IN CSV FILE",HttpStatus.OK);
    }
    @GetMapping("clients/see/all")
    public ResponseEntity<List<Client>> seeAllClients() throws IOException {
        List<Client> client = clientRepository.findAll();
        return new ResponseEntity<>(client,HttpStatus.OK);
    }
    @PostMapping("employee/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto)
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role roles= roleRepository.findByName("EMPLOYEE").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with Employee role",
                HttpStatus.OK);
    }
    @PostMapping("cashier/register")
    public ResponseEntity<String> cashierRegister(@RequestBody RegisterDto registerDto)
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles= roleRepository.findByName("CASHIER").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with Cashier",
                HttpStatus.OK);
    }
    @PostMapping("manager/register")
    public ResponseEntity<String> managerRegister(@RequestBody RegisterDto registerDto)
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles= roleRepository.findByName("MANAGER").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with Manager role",
                HttpStatus.OK);
    }

    @PostMapping("admin/register")
    public ResponseEntity<String> madminRegister(@RequestBody RegisterDto registerDto)
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
    @GetMapping("connected/see")
    public String seeUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
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


}
