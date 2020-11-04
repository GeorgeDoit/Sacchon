package com.sacchon.restapi.router;


import com.sacchon.restapi.resource.PingServerResource;
import com.sacchon.restapi.resource.impl.consultations.ConsultListResourceImpl;
import com.sacchon.restapi.resource.impl.consultations.ConsultResourceImpl;
import com.sacchon.restapi.resource.impl.consultations.ConsultsStatsResoureImpl;
import com.sacchon.restapi.resource.impl.doctor.DoctorListResourceImpl;
import com.sacchon.restapi.resource.impl.doctor.DoctorResourceImpl;
import com.sacchon.restapi.resource.impl.patient.*;
import com.sacchon.restapi.resource.impl.patient.measurements.PatientMeasurementImpl;
import com.sacchon.restapi.resource.impl.patient.measurements.PatientMeasurementListResourceImpl;
import com.sacchon.restapi.resource.impl.patient.measurements.PatientMeasurementsStatsResourceImpl;
import com.sacchon.restapi.resource.impl.user.UserListResourceImpl;
import com.sacchon.restapi.resource.impl.user.UserResourceImpl;
import com.sacchon.restapi.resource.interfaces.patient.SignUpResource;
import org.restlet.Application;
import org.restlet.routing.Router;

public class SacchonRouter {

    Application application;

    public SacchonRouter(Application application) {
        this.application = application;
    }


    public Router createRouter(){
        Router router = new Router(application.getContext());

        router.attach("/consults", ConsultListResourceImpl.class);
        router.attach("/consults/{id}", ConsultResourceImpl.class);

        router.attach("/stats/patient/avrg", PatientMeasurementsStatsResourceImpl.class);
        router.attach("/stats/patient/subs", PatientStatsSubsResourceImpl.class);
        router.attach("/stats/consult", ConsultsStatsResoureImpl.class);

        router.attach("/doctor", DoctorListResourceImpl.class);
        router.attach("/doctor/{id}", DoctorResourceImpl.class);

        router.attach("/measurements", PatientMeasurementListResourceImpl.class);
        router.attach("/measurements/{id}", PatientMeasurementImpl.class);

        router.attach("/patient/{id}", PatientResourceImpl.class);
        router.attach("/patient", PatientListResourceImpl.class);

        return router;
    }

    public Router publicResources() {
        Router router = new Router();

        router.attach("/ping", PingServerResource.class);
        router.attach("/signUp", SignUpResourceImpl.class);

        router.attach("/users", UserListResourceImpl.class);
        router.attach("/users/", UserListResourceImpl.class);
        router.attach("/user", UserResourceImpl.class);

        return router;
    }
}
