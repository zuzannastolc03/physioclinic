package com.example.physioclinic.dao;

import com.example.physioclinic.entity.*;
import org.springframework.security.core.Authentication;

import java.util.List;

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
    void addDiagnosis(Diagnosis diagnosis);
    List<?> getPatientsDiagnosis(Patient patient);
    Exercise findExerciseByName(String exerciseName);
    List<?> getListOfPictures();
    Diagnosis findDiagnosisById(int diagnosisId);
    List<?> getListOfTherapies(Diagnosis diagnosis);
    void addTherapy(Therapy therapy);
    Therapy findTherapyById(int therapyId);
    void updateTherapy(Therapy therapy);
    void deleteTherapy(int therapyId);
}
