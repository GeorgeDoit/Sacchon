package com.sacchon.restapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class PatientMeasurement {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    private double glucoseLevel;
    private int carbIntake;
    @Temporal(TemporalType.DATE)
    private Date dateStored;

    @ManyToOne
    private Patient patient;


}
