package com.zahir.fathurrahman.springsecurity.service;

import com.zahir.fathurrahman.springsecurity.domain.Role;
import com.zahir.fathurrahman.springsecurity.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);
    User getUser(String username);
    List<User> getUsers();

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
