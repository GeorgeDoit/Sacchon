package com.sacchon.restapi.repository;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.PatientMeasurement;
import com.sacchon.restapi.repository.lib.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class DoctorRepository extends Repository <Doctor, Long> {


    public DoctorRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Doctor> getEntityClass() {
        return Doctor.class;
    }

    @Override
    public String getEntityClassName() {
        return Doctor.class.getName();
    }

    public List<Doctor> findActiveDoctors(long start, long end) {
        if(start==0 || end==0){
            return  null;
        }
        TypedQuery<Doctor> query;
        java.util.Date startDate = new java.util.Date(start);
        java.util.Date endDate = new java.util.Date(end);
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.dateLastConsulted BETWEEN :startDate AND :endDate", getEntityClass())
                        .setParameter("startDate", startDate, TemporalType.DATE)
                        .setParameter("endDate", endDate, TemporalType.DATE);
        return query.getResultList();

    }

    public List<Doctor> findInactiveDoctors(long from,long to) {
        java.util.Date startDate = new java.util.Date(from);
        java.util.Date endDate = new java.util.Date(to);
        TypedQuery<Doctor> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.dateLastConsulted NOT BETWEEN :startDate AND :endDate" + " OR e.dateLastConsulted IS NULL", getEntityClass())
                .setParameter("startDate", startDate, TemporalType.DATE)
                .setParameter("endDate", endDate, TemporalType.DATE);
        return query.getResultList();

    }

    public List<Doctor> findDoctorByUsername(String username) {
        TypedQuery<Doctor> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.username = :username ", getEntityClass())
                .setParameter(Constants.QV_USERNAME,username);
        return query.getResultList();

    }


}
