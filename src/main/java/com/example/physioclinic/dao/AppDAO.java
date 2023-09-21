package com.example.physioclinic.dao;

import com.example.physioclinic.entity.Disease;
import com.example.physioclinic.entity.Patient;
import com.example.physioclinic.entity.Physiotherapist;
import com.example.physioclinic.entity.User;
import org.springframework.security.core.Authentication;

public interface AppDAO {
    User findUserByUsername(String username);
    String getLoggedUsername(Authentication authentication);
    String getLoggedAuthorities(Authentication authentication);
    void updateUser(User user);
    void addNewPatient(Patient patient);
    void addNewPhysiotherapist(Physiotherapist physiotherapist);
    void addDisease(Disease disease);
    Disease findDiseaseByName(String diseaseName);
    void updateDisease(Disease disease);
}
