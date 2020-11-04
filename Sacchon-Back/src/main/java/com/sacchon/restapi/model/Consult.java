package com.sacchon.restapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Consult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date dateStored;
    private boolean seen;

    @ManyToOne
    private Doctor doctor_consults;

    @ManyToOne
    private Patient patientToConsult;


}

