package com.sacchon.restapi.security;


public enum SacchonRole {
    ROLE_NA("n/a"),
    ROLE_PATIENT("patient"),
    ROLE_DOCTOR("doctor"),
    ROLE_ADMIN("admin");

    private final String roleName;

    SacchonRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static SacchonRole getRoleValue(String roleParameter) {
        for (SacchonRole role : SacchonRole.values()) {
            if (roleParameter.equals(role.getRoleName()))
                return role;
        }
        return ROLE_NA;
    }


}



