package com.example.physioclinic.controller;

import com.example.physioclinic.entity.*;
import com.example.physioclinic.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AppRestController {
    private AppService appService;

    @Autowired
    public AppRestController(AppService appService) {
        this.appService = appService;
    }

    @Operation(summary = "Home page.")
    @GetMapping("/")
    public String homePage() {
        return "This is a home page of a PhysioClinic app.";
    }

    @Operation(summary = "Physiotherapists' section.")
    @GetMapping("/physiotherapist")
    public String physioPage() {
        return "This is a section for physiotherapists.";
    }

    @Operation(summary = "Patients' section.")
    @GetMapping("/patient")
    public String patientPage() {
        return "This is a section for patients.";
    }

    @Operation(summary = "Gets data about who is logged in.")
    @GetMapping("/logged_username")
    public String getLoggedUsername(Authentication authentication) {
        return appService.getLoggedUsername(authentication);
    }

    @Operation(summary = "Gets data about logged in person's authorities.")
    @GetMapping("/logged_authorities")
    public String getLoggedAuthorities(Authentication authentication) {
        return appService.getLoggedAuthorities(authentication);
    }

    @Operation(summary = "Disables a user.")
    @PutMapping("/disable_user")
    public ResponseEntity<?> disableUser(@RequestParam String username) {
        User user = appService.findUserByUsername(username);
        if (user == null) {
            String errorMsg = "Something went wrong. Probably the username is incorrect.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        } else if (!user.isEnabled()) {
            String errorMsg = "User: " + user.getUsername() + " has already been disabled.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.disableUser(user);
        String msg = "Disabled user: " + user.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Changes password of a logged in user.")
    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestParam String newPassword) {
        String username = appService.getLoggedUsername(authentication);
        User user = appService.findUserByUsername(username);
        appService.changePassword(user, newPassword);
        String msg = "Password changed correctly.";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Adds a new patient with adequate inserts to users and authorities tables.")
    @PostMapping("/new_patient")
    public ResponseEntity<?> addNewPatient(@Valid @RequestBody Patient patient, @RequestParam @Email(message = "Invalid e-mail format.") String email, @RequestParam String password) {
        User user = appService.findUserByUsername(email);
        if (user != null) {
            String errorMsg = "User with e-mail address: " + email + " already exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        appService.addNewPatient(patient, email, password);
        String msg = "Congratulations " + patient.getFirstName() + " " + patient.getLastName() + ", you signed up successfully!";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Adds a new physiotherapist with adequate inserts to users and authorities tables.")
    @PostMapping("/new_physiotherapist")
    public ResponseEntity<?> addNewPhysiotherapist(@Valid @RequestBody Physiotherapist physiotherapist) {
        appService.addNewPhysiotherapist(physiotherapist);
        String msg = "Added a new physiotherapist: " + physiotherapist.getFirstName() + " " + physiotherapist.getLastName() + " with login: " + physiotherapist.getUser().getUsername() + ".";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Adds a new disease to the list.")
    @PostMapping("/add_disease")
    public ResponseEntity<?> addDisease(@Valid @RequestBody Disease disease) {
        appService.addDisease(disease);
        String msg = "Added a new disease: " + disease.getDiseaseName() + ".";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Updates an existing disease.")
    @PutMapping("/update_disease")
    public ResponseEntity<?> updateDisease(@Valid @RequestBody Disease disease, @RequestParam String diseaseName) {
        Disease tempDisease = appService.findDiseaseByName(diseaseName);
        if (tempDisease == null) {
            String errorMsg = "Disease " + diseaseName + " doesn't exist, so you can't update it.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        appService.updateDisease(disease, diseaseName);
        String msg = "Updated disease: " + diseaseName + " to disease: " + disease.getDiseaseName() + " with description: " + disease.getDescription() + ".";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Displays disease's details.")
    @GetMapping("/display_disease")
    public ResponseEntity<?> displayDisease(@RequestParam String diseaseName){
        Disease disease = appService.findDiseaseByName(diseaseName);
        if(disease == null){
            String errorMsg = "Disease " + diseaseName + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        String msg = "Name: " + disease.getDiseaseName() + "\n" + "Description: " + disease.getDescription();
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Adds a new diagnosis to the list.")
    @PostMapping("/add_diagnosis")
    public ResponseEntity<?> addDiagnosis(Authentication authentication, @RequestParam String patientsUsername, @RequestParam String diseasesName) {
        Physiotherapist physiotherapist = appService.findPhysiotherapistByUsername(appService.getLoggedUsername(authentication));
        Patient patient = appService.findPatientByUsername(patientsUsername);
        if(patient == null){
            String errorMsg = "Patient " + patientsUsername + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        Disease disease = appService.findDiseaseByName(diseasesName);
        if(disease == null){
            String errorMsg = "Disease " + diseasesName + " doesn't exist.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
        }
        Diagnosis diagnosis = new Diagnosis();
        appService.addDiagnosis(diagnosis, physiotherapist, patient, disease);
        String msg = "Successfully added a new diagnosis.";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @Operation(summary = "Gets a list of diagnosis of a logged in patient (patient's own view).")
    @GetMapping("/list_of_my_diagnosis")
    public List<?> getPatientsDiagnosis(Authentication authentication) {
        User user = appService.findUserByUsername(appService.getLoggedUsername(authentication));
        Patient patient = user.getPatient();
        return appService.getPatientsDiagnosis(patient);
    }

    @Operation(summary = "Gets a list of diagnosis of an indicated patient (physiotherapist's view).")
    @GetMapping("/list_of_patients_diagnosis")
    public List<?> getPatientsDiagnosis(@RequestParam String username) {
        User user = appService.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User: " + username + " doesn't exist.");
        }
        Patient patient = user.getPatient();
        if (patient == null) {
            throw new RuntimeException("User: " + username + " is not a patient.");
        }
        return appService.getPatientsDiagnosis(patient);
    }

    @Operation(summary = "Generates a name of a .jpg in which the proper way of making an exercise is shown.")
    @GetMapping("/exercises_picture")
    public String getExercisesPicturesName(@RequestParam String exerciseName) {
        Exercise exercise = appService.findExerciseByName(exerciseName);
        if (exercise == null) {
            throw new RuntimeException("Exercise: " + exerciseName + " doesn't exist.");
        }
        return exerciseName.replaceAll(" ", "_").toLowerCase().concat(".jpg");
    }

    @Operation(summary = "Gets a list of available pictures.")
    @GetMapping("/exercises_list")
    public List<?> getExercisesPicturesName() {
        System.out.println(appService.getListOfPictures());
        return appService.getListOfPictures();
    }
}
