package com.sacchon.restapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserTable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    private String username;
    private String password;
    private String role;
    private boolean active;


    @OneToOne
    private Patient patient;

    @OneToOne
    private Doctor doctor;

    @OneToOne
    private Admin admin;
}