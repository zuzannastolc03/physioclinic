package com.example.physioclinic.service;

import com.example.physioclinic.dao.AppDAO;
import com.example.physioclinic.entity.*;
import jdk.jshell.Diag;
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
import java.util.List;
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

    @Override
    @Transactional
    public void addNewPatient(Patient patient, String email, String password) {
        password = passwordEncoder.encode(password);
        User user = new User(email, password, true);
        Authority authority = new Authority("ROLE_PATIENT");
        user.addAuthority(authority);
        patient.setUser(user);
        appDAO.addNewPatient(patient);
    }


    @Override
    @Transactional
    public void addNewPhysiotherapist(Physiotherapist physiotherapist) {
        String username = generateUsername(physiotherapist.getFirstName(), physiotherapist.getLastName(), 0);
        String password = passwordEncoder.encode(physiotherapist.getFirstName().toLowerCase());
        User user = new User(username, password, true);
        Authority authority = new Authority("ROLE_PHYSIOTHERAPIST");
        user.addAuthority(authority);
        physiotherapist.setUser(user);
        appDAO.addNewPhysiotherapist(physiotherapist);
    }

    @Override
    @Transactional
    public void addDisease(Disease disease) {
        appDAO.addDisease(disease);
    }

    @Override
    public Disease findDiseaseByName(String diseaseName) {
        return appDAO.findDiseaseByName(diseaseName);
    }

    @Override
    @Transactional
    public void updateDisease(Disease disease, String diseaseName) {
        Disease tempDisease = appDAO.findDiseaseByName(diseaseName);
        tempDisease.setDiseaseName(disease.getDiseaseName());
        tempDisease.setDescription(disease.getDescription());
        appDAO.updateDisease(tempDisease);
    }

    @Override
    public Patient findPatientByUsername(String username) {
        User user = findUserByUsername(username);
        Patient patient;
        try {
            patient = user.getPatient();
        } catch (Exception ex) {
            patient = null;
        }
        return patient;
    }

    @Override
    public Physiotherapist findPhysiotherapistByUsername(String username) {
        User user = findUserByUsername(username);
        Physiotherapist physiotherapist;
        try {
            physiotherapist = user.getPhysiotherapist();
        } catch (Exception ex) {
            physiotherapist = null;
        }
        return physiotherapist;
    }

    @Override
    @Transactional
    public void addDiagnosis(Diagnosis diagnosis, Physiotherapist physiotherapist, Patient patient, Disease disease) {
        diagnosis.setPhysiotherapist(physiotherapist);
        diagnosis.setPatient(patient);
        diagnosis.setDisease(disease);
        appDAO.addDiagnosis(diagnosis);
    }

    @Override
    public List<?> getPatientsDiagnosis(Patient patient) {
        return appDAO.getPatientsDiagnosis(patient);
    }

    @Override
    public Exercise findExerciseByName(String exerciseName) {
        return appDAO.findExerciseByName(exerciseName);
    }

    @Override
    public List<?> getListOfPictures() {
        return appDAO.getListOfPictures();
    }

    public String generateUsername(String firstName, String lastName, int i) {
        String username = firstName.toLowerCase() + "." + lastName.toLowerCase();
        if (i != 0) {
            username = username + i;
        }
        username = username + "@physioclinic.com";
        User user = findUserByUsername(username);
        if (user != null) {
            return generateUsername(firstName, lastName, i + 1);
        } else {
            return username;
        }
    }
}
