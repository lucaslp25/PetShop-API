package com.lucasdev.petshop_api.security.services;


import com.lucasdev.petshop_api.security.exceptions.PetShopLoginException;
import com.lucasdev.petshop_api.security.exceptions.PetShopSecurityException;
import com.lucasdev.petshop_api.security.model.DTO.RegisterResponseDTO;
import com.lucasdev.petshop_api.security.model.DTO.TokenDTO;
import com.lucasdev.petshop_api.security.model.DTO.UserLoginDTO;
import com.lucasdev.petshop_api.security.model.DTO.UserRegisterDTO;
import com.lucasdev.petshop_api.security.model.User;
import com.lucasdev.petshop_api.security.model.UserRole;
import com.lucasdev.petshop_api.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PetShopTokenService tokenService;


    @Transactional
    public RegisterResponseDTO register(UserRegisterDTO dtoRef){

        if (dtoRef.username() == null || dtoRef.password() == null){
            throw new PetShopLoginException("Error in make register of username: one or more fields empty");
        }

        if (dtoRef.role() != UserRole.EMPLOYEE && dtoRef.role() != UserRole.CUSTOMER){
            throw new PetShopLoginException("Error in make register of role: INCORRECT NAME ROLE.");
        }

        if (repository.findByLogin(dtoRef.username()).isPresent()){
            throw new PetShopSecurityException("Already exists this username.");
        }

        String pass = passwordEncoder.encode(dtoRef.password());

        User user = new User(dtoRef.username(), pass, dtoRef.role());

        user = repository.save(user);

        String message = "Register Successfully!\nPut your login in another endpoint for take authorization";

        return new RegisterResponseDTO(user, message);
    }

    @Transactional
    public TokenDTO login(UserLoginDTO dtoRef){

        if (dtoRef.username() == null || dtoRef.password() == null){
            throw new PetShopLoginException("Error in make register of username: one or more fields empty");
        }

        var pass = new UsernamePasswordAuthenticationToken(dtoRef.username(), dtoRef.password());

        var auth = authenticationManager.authenticate(pass);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return new TokenDTO(token);
    }
}