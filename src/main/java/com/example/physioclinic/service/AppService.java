package com.example.physioclinic.service;

import com.example.physioclinic.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppService extends UserDetailsService {
    public User findUserByUsername(String username);
    String getLoggedUsername(Authentication authentication);

    String getLoggedAuthorities(Authentication authentication);
    void disableUser(User user);
    void changePassword(User user, String newPassword);
}
