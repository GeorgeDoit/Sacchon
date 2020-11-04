package com.sacchon.restapi.representations.consultations;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ConsultsStatsRepresentation {
    private long subs;

    static public ConsultsStatsRepresentation getConsultsStatsRepresentation(long subs){
        ConsultsStatsRepresentation consultsStatsRepresentation = new ConsultsStatsRepresentation();
        consultsStatsRepresentation.setSubs(subs);
        return consultsStatsRepresentation;
    }

}
