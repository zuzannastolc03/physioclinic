package com.example.physioclinic.service;

import com.example.physioclinic.entity.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppService extends UserDetailsService {
    User findUserByUsername(String username);
    String getLoggedUsername(Authentication authentication);

    String getLoggedAuthorities(Authentication authentication);
    void disableUser(User user);
    void changePassword(User user, String newPassword);
    void addNewPatient(Patient patient, String email, String password);
    void addNewPhysiotherapist(Physiotherapist physiotherapist);
    void addDisease(Disease disease);
    Disease findDiseaseByName(String diseaseName);
    void updateDisease(Disease disease, String diseaseName);
    Patient findPatientByUsername(String username);
    Physiotherapist findPhysiotherapistByUsername(String username);
    void addDiagnosis(Diagnosis diagnosis, Physiotherapist physiotherapist, Patient patient, Disease disease);
    List<?> getPatientsDiagnosis(Patient patient);
}
