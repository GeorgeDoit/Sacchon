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
public class Doctor {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    private String name;
    private String surname;
    private String username;
    private String password;

    @Temporal(TemporalType.DATE)
    private Date dateLastConsulted;

    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients = new ArrayList<>();

    @OneToMany (mappedBy = "doctor_consults")
    private List<Consult> consults = new ArrayList();

}
