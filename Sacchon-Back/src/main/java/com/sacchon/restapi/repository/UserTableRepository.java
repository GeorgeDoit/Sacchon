package com.sacchon.restapi.repository;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.UserTable;
import com.sacchon.restapi.repository.lib.Repository;
import com.sacchon.restapi.security.SacchonRole;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserTableRepository extends Repository<UserTable, Long> {

    public UserTableRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<UserTable> getEntityClass() {
        return UserTable.class;
    }

    @Override
    public String getEntityClassName() {
        return UserTable.class.getName();
    }

    public Optional<UserTable> findUser(long id, String role) {
        TypedQuery<UserTable> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e", getEntityClass());
        if(role.equals(SacchonRole.ROLE_PATIENT.getRoleName())){
            query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.patient.id =:id", getEntityClass())
                    .setParameter("id",id);
        }else if(role.equals(SacchonRole.ROLE_ADMIN.getRoleName())){
            query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.admin.id =:id ", getEntityClass())
                    .setParameter("id",id);
        }else if(role.equals(SacchonRole.ROLE_DOCTOR.getRoleName())){
            query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.doctor.id =:id ", getEntityClass())
                    .setParameter("id",id);
        }
         return Optional.of(query.getSingleResult());
    }

    public Boolean findUser(String username) {
        TypedQuery<Long> query;
        query = getEntityManager().createQuery(" SELECT COUNT(e) FROM " + getEntityClassName() + " e WHERE  e.username = :username ", Long.class)
                .setParameter(Constants.QV_USERNAME,username);
        Long count = (Long)query.getSingleResult();
        return (!count.equals(0L));
    }

    public Optional<UserTable> findUserInstance(String username,String password){
        TypedQuery<UserTable> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE e.username =:username" + " AND e.password =:password" + " AND e.active =:active" , getEntityClass())
                    .setParameter(Constants.QV_USERNAME,username)
                    .setParameter(Constants.QV_PASSWORD,password)
                    .setParameter("active",true);
        return Optional.of(query.getSingleResult());
    }

}
