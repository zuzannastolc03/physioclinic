package com.example.physioclinic.dao;

import com.example.physioclinic.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

@Repository
public class AppDAOImpl implements AppDAO{
    private EntityManager entityManager;

    @Autowired
    public AppDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public User findUserByUsername(String username) {
        Query theQuery = entityManager.createNativeQuery("SELECT * FROM physioclinic.users where username = :username", User.class);
        theQuery.setParameter("username", username);
        User user;
        try {
            user = (User)theQuery.getSingleResult();
        } catch (Exception e) {
            user = null;
        }
        return user;
    }

    @Override
    public String getLoggedUsername(Authentication authentication) {
        return authentication.getName();
    }

    @Override
    public String getLoggedAuthorities(Authentication authentication) {
        return authentication.getAuthorities().toString();
    }

    @Override
    public void updateUser(User user) {
        entityManager.persist(user);
    }
}
