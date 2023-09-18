package com.example.physioclinic.service;

import com.example.physioclinic.dao.AppDAO;
import com.example.physioclinic.entity.Authority;
import com.example.physioclinic.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AppServiceImpl implements AppService{
    private AppDAO appDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AppServiceImpl(AppDAO appDAO, PasswordEncoder passwordEncoder) {
        this.appDAO = appDAO;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = appDAO.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getAuthorities()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Authority> authorities) {
        return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList());
    }

    @Override
    public User findUserByUsername(String username) {
        return appDAO.findUserByUsername(username);
    }

    @Override
    public String getLoggedUsername(Authentication authentication) {
        return appDAO.getLoggedUsername(authentication);
    }

    @Override
    public String getLoggedAuthorities(Authentication authentication) {
        return appDAO.getLoggedAuthorities(authentication);
    }

    @Override
    @Transactional
    public void disableUser(User user) {
        user.setEnabled(false);
        appDAO.updateUser(user);
    }

    @Override
    @Transactional
    public void changePassword(User user, String newPassword) {
        String password = passwordEncoder.encode(newPassword);
        user.setPassword(password);
        appDAO.updateUser(user);
    }
}
