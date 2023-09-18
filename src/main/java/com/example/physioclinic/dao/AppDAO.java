package com.example.physioclinic.dao;

import com.example.physioclinic.entity.User;
import org.springframework.security.core.Authentication;

public interface AppDAO {
    User findUserByUsername(String username);
    String getLoggedUsername(Authentication authentication);
    String getLoggedAuthorities(Authentication authentication);
    void updateUser(User user);
}
