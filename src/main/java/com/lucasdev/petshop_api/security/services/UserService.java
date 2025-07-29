package com.lucasdev.petshop_api.security.services;


import com.lucasdev.petshop_api.exceptions.ResourceNotFoundException;
import com.lucasdev.petshop_api.model.DTO.LoginResponseDTO;
import com.lucasdev.petshop_api.security.exceptions.PetShopLoginException;
import com.lucasdev.petshop_api.security.exceptions.PetShopSecurityException;
import com.lucasdev.petshop_api.security.model.DTO.*;
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

        String message = "Register Successfully! Put your login in another endpoint for take authorization";

        return new RegisterResponseDTO(user, message);
    }

    @Transactional
    public LoginResponseDTO login(UserLoginDTO dtoRef){

        if (dtoRef.username() == null || dtoRef.password() == null){
            throw new PetShopLoginException("Error in make register of username: one or more fields empty");
        }

        var pass = new UsernamePasswordAuthenticationToken(dtoRef.username(), dtoRef.password());

        var auth = authenticationManager.authenticate(pass);

        var authenticatedUser = (User) auth.getPrincipal();

        String token = tokenService.generateToken(authenticatedUser);

        String name = null;
        String email = null;

        if (authenticatedUser.getAuthorities().contains(UserRole.CUSTOMER)){

            name = authenticatedUser.getCustomerProfile().getName();
            email = authenticatedUser.getCustomerProfile().getEmail();

        }else {
            name = authenticatedUser.getEmployeeProfile().getName();
            email = authenticatedUser.getEmployeeProfile().getEmail();

        }
        return new LoginResponseDTO(token, name, email, authenticatedUser.getRole().getRole());
    }

    @Transactional(readOnly = true)
    public User findUserByLogin(String login){

        User user = repository.findUserByLogin(login).orElseThrow(() ->
                new ResourceNotFoundException("Not found user with login: " + login));

        return user;
    }
}