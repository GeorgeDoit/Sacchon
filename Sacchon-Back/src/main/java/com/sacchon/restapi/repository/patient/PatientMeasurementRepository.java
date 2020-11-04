package com.sacchon.restapi.repository.patient;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.PatientMeasurement;
import com.sacchon.restapi.repository.lib.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class PatientMeasurementRepository extends Repository<PatientMeasurement, Long> {

    public PatientMeasurementRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Class<PatientMeasurement> getEntityClass() {
        return PatientMeasurement.class;
    }

    public String getEntityClassName() {
        return PatientMeasurement.class.getName();
    }

    public List<PatientMeasurement> findPatientMeasurements(long id, long start, long end) {

        TypedQuery<PatientMeasurement> query;

        if(start == 0 && end == 0){
            query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e  WHERE e.patient.id = " + String.valueOf(id), getEntityClass());
        }else{
            java.util.Date startDate = new java.util.Date(start);
            java.util.Date endDate = new Date(end);
            query = getEntityManager().createQuery(" SELECT e FROM " + getEntityClassName() + " e WHERE  e.dateStored BETWEEN :startDate AND :endDate" + " AND e.patient.id = " + String.valueOf(id), getEntityClass())
                    .setParameter("startDate", startDate, TemporalType.DATE)
                    .setParameter("endDate", endDate, TemporalType.DATE);
        }

        return query.getResultList();

    }

    public long findPatientStatsSubs(long id, long start, long end) {

        TypedQuery<Long> query;

        if(start == 0 && end == 0){
            query = getEntityManager().createQuery(" SELECT COUNT(*) FROM " + getEntityClassName() + " e WHERE  e.patient.id = " + id, Long.class)
                   ;
        }else{
            java.util.Date startDate = new java.util.Date(start);
            java.util.Date endDate = new java.util.Date(end);
            String q = " SELECT COUNT(*) FROM " + getEntityClassName() + " e WHERE  e.dateStored BETWEEN :startDate AND :endDate" + " AND e.patient.id = " + id;
            query = getEntityManager().createQuery(q, Long.class)
                    .setParameter("startDate", startDate, TemporalType.DATE)
                    .setParameter("endDate", endDate, TemporalType.DATE)
                    ;
        }


        return query.getSingleResult();

    }
}
