package com.example.physioclinic.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "physiotherapists")
public class Physiotherapist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "physiotherapist_id")
    private int physiotherapistId;
    @Column(name = "first_name")
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Invalid first name.")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Invalid last name.")
    @NotBlank
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Physiotherapist() {
    }

    public Physiotherapist(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getPhysiotherapistId() {
        return physiotherapistId;
    }

    public void setPhysiotherapistId(int physiotherapistId) {
        this.physiotherapistId = physiotherapistId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Physiotherapist{" +
                "physiotherapistId=" + physiotherapistId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
