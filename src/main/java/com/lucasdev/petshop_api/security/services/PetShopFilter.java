package com.lucasdev.petshop_api.security.services;

import com.lucasdev.petshop_api.security.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PetShopFilter extends OncePerRequestFilter {

    private final PetShopTokenService petShopTokenService;

    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = this.recoverToken(request);
        if (token != null) {

            var login = petShopTokenService.validateToken(token);

            if (login != null && !login.isEmpty()) {

                var user = userRepository.findByLogin(login);

                if (user != null) {

                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.get().getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {

        var header = request.getHeader("Authorization");

        if (header == null) {
            return null;
        }

        return header.replace("Bearer ", "");
    }
}
