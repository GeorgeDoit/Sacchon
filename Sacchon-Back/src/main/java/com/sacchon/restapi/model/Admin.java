package com.sacchon.restapi.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity

public class Admin {
       @Id
       @GeneratedValue(
            strategy = GenerationType.IDENTITY
       )

       private long id;
       private String username;
       private String password;
}
