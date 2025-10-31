package net.eyone.yonewi_mapping.domain.prestation.dtos.analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private Integer identifier;
    private Integer doctorId;
    private String doctorUsername;
    private String doctorCompleteName;
    private Boolean ordonanceByDoctor;
    private Boolean facturedByDoctor;
}
