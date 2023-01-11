package com.zahir.fathurrahman.springsecurity.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zahir.fathurrahman.springsecurity.domain.Role;
import com.zahir.fathurrahman.springsecurity.domain.User;
import com.zahir.fathurrahman.springsecurity.repo.RoleRepo;
import com.zahir.fathurrahman.springsecurity.repo.UserRepo;
import com.zahir.fathurrahman.springsecurity.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service @RequiredArgsConstructor @Slf4j @Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Username is not registered yet");
        }
        Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new user to the database");
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        String test = "";
        System.out.println(test);
        return userRepo.findAll();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = JWTUtil.decodedJWT(refresh_token);
                User user =  userRepo.findByUsername(decodedJWT.getSubject());
                List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
                String newAccessToken = JWTUtil.generateAccessToken(request,user.getUsername(),roles);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",newAccessToken);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception exception){
                log.error("Error logging in : {}",exception.getMessage());
                response.setHeader("error",exception.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        } else {
            throw new RuntimeException("Bearer Auth is required");
        }
    }
}
