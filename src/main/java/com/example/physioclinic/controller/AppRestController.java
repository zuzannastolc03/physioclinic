package com.example.physioclinic.controller;

import com.example.physioclinic.entity.Patient;
import com.example.physioclinic.entity.Physiotherapist;
import com.example.physioclinic.entity.User;
import com.example.physioclinic.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

}
