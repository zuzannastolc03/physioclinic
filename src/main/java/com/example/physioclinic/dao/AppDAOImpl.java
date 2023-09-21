package com.example.physioclinic.dao;

import com.example.physioclinic.entity.Disease;
import com.example.physioclinic.entity.Patient;
import com.example.physioclinic.entity.Physiotherapist;
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

    @Override
    public void addNewPatient(Patient patient) {
        entityManager.persist(patient);
    }

    @Override
    public void addNewPhysiotherapist(Physiotherapist physiotherapist) {
        entityManager.persist(physiotherapist);
    }

    @Override
    public void addDisease(Disease disease) {
        entityManager.persist(disease);
    }

    @Override
    public Disease findDiseaseByName(String diseaseName) {
        Query theQuery = entityManager.createNativeQuery("SELECT * FROM physioclinic.diseases where disease_name = :diseaseName", Disease.class);
        theQuery.setParameter("diseaseName", diseaseName);
        Disease disease;
        try {
            disease = (Disease)theQuery.getSingleResult();
        } catch (Exception e) {
            disease = null;
        }
        return disease;
    }

    @Override
    public void updateDisease(Disease disease) {
        entityManager.persist(disease);
    }
}
