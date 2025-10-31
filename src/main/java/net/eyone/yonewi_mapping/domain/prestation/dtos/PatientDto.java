package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    private Integer identifier;
    private String eyoneInternalId;
    private String firstName;
    private String lastName;
    private SexDto sex;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SexDto {
        private String code;
        private String label;
    }
}
