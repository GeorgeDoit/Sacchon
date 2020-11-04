package com.sacchon.restapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Patient {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    private String name;
    private String surname;
    @Temporal(TemporalType.DATE)
    private Date dob;
    private long amka;
    private Gender gender;
    private boolean availableConsulted;
    private String username;
    private String password;
    @Temporal(TemporalType.DATE)
    private Date dateAccountCreated;
    @Temporal(TemporalType.DATE)
    private Date dateLastConsulted;
    @Temporal(TemporalType.DATE)
    private Date dateMonthStarted;
    @Temporal(TemporalType.DATE)
    private Date dateLastStored;
    @Temporal(TemporalType.DATE)
    private Date dateAvailableConsulted;
    private boolean warning;

    @OneToMany(
            mappedBy = "patient"
    )
    private List<PatientMeasurement> patientMeasurements = new ArrayList();

    @OneToMany(mappedBy = "patientToConsult")
    private List<Consult> patientConsultations = new ArrayList();

    @OneToOne(mappedBy = "patient")
    private UserTable user;

    @ManyToOne
    Doctor doctor;
}
