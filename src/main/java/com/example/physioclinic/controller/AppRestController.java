package com.example.physioclinic.controller;

import com.example.physioclinic.entity.User;
import com.example.physioclinic.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
