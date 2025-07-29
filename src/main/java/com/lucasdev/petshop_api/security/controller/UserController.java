package com.lucasdev.petshop_api.security.controller;

import com.lucasdev.petshop_api.model.DTO.LoginResponseDTO;
import com.lucasdev.petshop_api.security.model.DTO.RegisterResponseDTO;
import com.lucasdev.petshop_api.security.model.DTO.TokenDTO;
import com.lucasdev.petshop_api.security.model.DTO.UserLoginDTO;
import com.lucasdev.petshop_api.security.model.DTO.UserRegisterDTO;
import com.lucasdev.petshop_api.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor //dependency injection in another level than Autowired
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLoginDTO dtoRef) {

        LoginResponseDTO dto = userService.login(dtoRef);

        return ResponseEntity.ok().body(dto); //code 200 here think is more semantic
    }

    @PostMapping(value = "/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody UserRegisterDTO dtoRef) {

        RegisterResponseDTO dto = userService.register(dtoRef);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}").buildAndExpand(dto.username()).toUri();

        return ResponseEntity.created(uri).body(dto); //code201
    }

}