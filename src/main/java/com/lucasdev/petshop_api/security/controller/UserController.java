package com.lucasdev.petshop_api.security.controller;

import com.lucasdev.petshop_api.security.exceptions.PetShopSecurityException;
import com.lucasdev.petshop_api.security.model.DTO.TokenDTO;
import com.lucasdev.petshop_api.security.model.DTO.UserRequestDTO;
import com.lucasdev.petshop_api.security.model.User;
import com.lucasdev.petshop_api.security.repositories.UserRepository;
import com.lucasdev.petshop_api.security.services.PetShopTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor //dependency injection in another level than Autowired
public class UserController {

    private final UserRepository repository;

    private final AuthenticationManager authenticationManager;

    private final PetShopTokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/login")
    public ResponseEntity<TokenDTO> login(@RequestBody  UserRequestDTO dtoRef) {

        //new
        var pass = new UsernamePasswordAuthenticationToken(dtoRef.username(), dtoRef.password());

        var auth = authenticationManager.authenticate(pass);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenDTO(token));
    }

    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody UserRequestDTO dtoRef) {

        if (repository.findByLogin(dtoRef.username()).isPresent()){
            throw new PetShopSecurityException("Already exists this username.");
        }

        String pass = passwordEncoder.encode(dtoRef.password());

        User user = new User(dtoRef.username(), pass, dtoRef.role());

        repository.save(user);

        return ResponseEntity.ok().build();
    }

}