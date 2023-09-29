package com.example.physioclinic.dao;

import com.example.physioclinic.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void addDiagnosis(Diagnosis diagnosis) {
        entityManager.persist(diagnosis);
    }

    @Override
    public List<?> getPatientsDiagnosis(Patient patient) {
        Query theQuery = entityManager.createNativeQuery("SELECT * FROM physioclinic.diagnosis where patient_id = :patientId", Diagnosis.class);
        theQuery.setParameter("patientId", patient.getPatientId());
        return theQuery.getResultList();
    }

    @Override
    public Exercise findExerciseByName(String exerciseName) {
        Query theQuery = entityManager.createNativeQuery("SELECT * FROM physioclinic.exercises where exercise_name = :exerciseName", Exercise.class);
        theQuery.setParameter("exerciseName", exerciseName);
        Exercise exercise;
        try {
            exercise = (Exercise) theQuery.getSingleResult();
        } catch (Exception e) {
            exercise = null;
        }
        return exercise;
    }

    @Override
    public List<?> getListOfPictures() {
        List<String> fileList = new ArrayList<>();
        File directory = new File("src/main/resources/images");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file.getName());
                }
            }
        }
        return fileList;
    }

    @Override
    public Diagnosis findDiagnosisById(int diagnosisId) {
        return entityManager.find(Diagnosis.class, diagnosisId);
    }

    @Override
    public List<?> getListOfTherapies(Diagnosis diagnosis) {
        Query theQuery = entityManager.createNativeQuery("SELECT * FROM physioclinic.therapies where diagnosis_id = :diagnosisId", Therapy.class);
        theQuery.setParameter("diagnosisId", diagnosis.getDiagnosisId());
        return theQuery.getResultList();
    }

    @Override
    public void addTherapy(Therapy therapy) {
        entityManager.persist(therapy);
    }

    @Override
    public Therapy findTherapyById(int therapyId) {
        return entityManager.find(Therapy.class, therapyId);
    }

    @Override
    public void updateTherapy(Therapy therapy) {
        entityManager.persist(therapy);
    }

    @Override
    public void deleteTherapy(int therapyId) {
        Therapy therapy = findTherapyById(therapyId);
        entityManager.remove(therapy);
    }
}
