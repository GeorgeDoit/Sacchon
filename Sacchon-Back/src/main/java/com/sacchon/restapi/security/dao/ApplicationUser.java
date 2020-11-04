package com.sacchon.restapi.security.dao;

import com.sacchon.restapi.security.SacchonRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser{
    private String username;
    private String password;
    private SacchonRole role;
}