package com.sacchon.restapi.repository;

import com.sacchon.restapi.model.Consult;
import com.sacchon.restapi.repository.lib.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class ConsultRepository extends Repository<Consult, Long> {

    public ConsultRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Consult> getEntityClass() {
        return Consult.class;
    }

    @Override
    public String getEntityClassName() {
        return Consult.class.getName();
    }



    public long numPatientConsults(long id, long start, long end) {
        TypedQuery<Long> query;
        if(start!= 0 && end != 0) {
            java.util.Date startDate = new java.util.Date(start);
            java.util.Date endDate = new Date(end);
            String qr = " SELECT COUNT(*) FROM " + getEntityClassName() + " e WHERE  e.dateStored BETWEEN :startDate AND :endDate" + " AND e.patientToConsult.id = " + id;
            query = getEntityManager().createQuery(qr, Long.class)
                    .setParameter("startDate", startDate, TemporalType.DATE)
                    .setParameter("endDate", endDate, TemporalType.DATE);
        }else{
            String qr = " SELECT COUNT(e) FROM " + getEntityClassName() + " e  WHERE e.patientToConsult.id = " + id;
            query = getEntityManager().createQuery(qr, Long.class);
        }

        return query.getSingleResult();
    }

    public long numDoctorConsults(long id, long start, long end) {
        TypedQuery<Long> query;
        if(start!= 0 && end != 0) {
            java.util.Date startDate = new java.util.Date(start);
            java.util.Date endDate = new Date(end);
            String qr = " SELECT COUNT(*) FROM " + getEntityClassName() + " e WHERE  e.dateStored BETWEEN :startDate AND :endDate" + " AND e.doctor_consults.id = " + id;
            query = getEntityManager().createQuery(qr, Long.class)
                    .setParameter("startDate", startDate, TemporalType.DATE)
                    .setParameter("endDate", endDate, TemporalType.DATE);
        }else{
            String qr = " SELECT COUNT(*) FROM " + getEntityClassName() + " e WHERE  e.doctor_consults.id = " + id;
            query = getEntityManager().createQuery(qr, Long.class);
        }


        return query.getSingleResult();
    }

    public long numConsults(long pid,long did, long start, long end) {
        TypedQuery<Long> query;
        if(start!= 0 && end != 0) {
            java.util.Date startDate = new java.util.Date(start);
            java.util.Date endDate = new Date(end);
            String qr = " SELECT COUNT(*) FROM " + getEntityClassName() + " e WHERE  e.dateStored BETWEEN :startDate AND :endDate" + " AND e.doctor_consults.id = " + did + " AND e.patientToConsult.id = " + pid;
            query = getEntityManager().createQuery(qr, Long.class)
                    .setParameter("startDate", startDate, TemporalType.DATE)
                    .setParameter("endDate", endDate, TemporalType.DATE);
        }else{
            String qr = " SELECT COUNT(*) FROM " + getEntityClassName() + " e WHERE  e.doctor_consults.id = " + did + " AND e.patientToConsult.id = " + pid;
            query = getEntityManager().createQuery(qr, Long.class);
        }


        return query.getSingleResult();
    }

    public List<Consult> findPatientConsults(long patient_id) {
        TypedQuery<Consult> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e  WHERE e.patientToConsult.id = :patient_id ", getEntityClass())
                    .setParameter("patient_id",patient_id);
        return query.getResultList();
    }
    public List<Consult> findDoctorConsults(long doctor_id) {
        TypedQuery<Consult> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e  WHERE e.doctor_consults.id = :doctor_id ", getEntityClass())
                    .setParameter("doctor_id",doctor_id);
        return query.getResultList();
    }
    public List<Consult> findConsults(long patient_id,long doctor_id) {
        TypedQuery<Consult> query;
        query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e  WHERE e.patientToConsult.id = :patient_id " + " AND e.doctor_consults.id = :doctor_id ", getEntityClass())
                    .setParameter("patient_id",patient_id)
                    .setParameter("doctor_id",doctor_id);


        return query.getResultList();
    }
}
