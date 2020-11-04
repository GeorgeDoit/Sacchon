package com.sacchon.restapi.repository.patient;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.repository.lib.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class PatientRepository extends Repository<Patient, Long> {

    public PatientRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Class<Patient> getEntityClass() {
        return Patient.class;
    }

    public String getEntityClassName() {
        return Patient.class.getName();
    }

    public List<Patient> findPatientMeasurements(long id, long start, long end) {

        TypedQuery<Patient> query;

        if(start == 0 && end == 0){
            query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e  WHERE e.patient.id = " + String.valueOf(id), getEntityClass());
        }else if(start!=0 && end == 0){
            String q = " SELECT e FROM " + getEntityClassName() + " e  WHERE e.dateStored > :startDate AND e.patient.id = " + String.valueOf(id);
            java.util.Date startDate = new java.util.Date(start);
            query = getEntityManager().createQuery(q, getEntityClass())
                    .setParameter("startDate", startDate, TemporalType.DATE);
        }else{
            java.util.Date startDate = new java.util.Date(start);
            java.util.Date endDate = new Date(end);
            query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.dateStored BETWEEN :startDate AND :endDate" + " AND e.patient.id = " + String.valueOf(id), getEntityClass())
                    .setParameter("startDate", startDate, TemporalType.DATE)
                    .setParameter("endDate", endDate, TemporalType.DATE);
        }

        return query.getResultList();

    }

    public List<Patient> findPatientsByDocId(long id) {
        TypedQuery<Patient> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.doctor.id = :id ", getEntityClass())
                .setParameter("id",id);
        return query.getResultList();

    }

    public List<Patient> findPatientsByUsername(String username) {
        TypedQuery<Patient> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.username = :username ", getEntityClass())
                .setParameter(Constants.QV_USERNAME,username);
        return query.getResultList();

    }

    public List<Patient> findAvailablePatients() {
        TypedQuery<Patient> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.availableConsulted = :available", getEntityClass())
                .setParameter("available", true);
        return query.getResultList();

    }

    public List<Patient> findFreePatients() {
        TypedQuery<Patient> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.doctor IS NULL AND e.availableConsulted = :available", getEntityClass())
                .setParameter("available", true);
        return query.getResultList();

    }

    public List<Patient> findInactivePatients(long from,long to) {
        java.util.Date startDate = new java.util.Date(from);
        java.util.Date endDate = new java.util.Date(to);
        TypedQuery<Patient> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.dateLastStored NOT BETWEEN :startDate AND :endDate"+ " OR e.dateLastStored IS NULL", getEntityClass())
                .setParameter("startDate", startDate, TemporalType.DATE)
                .setParameter("endDate", endDate, TemporalType.DATE);
        return query.getResultList();

    }







}
