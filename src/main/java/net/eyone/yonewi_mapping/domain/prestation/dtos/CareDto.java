package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CareDto {
    private Integer identifier;
    private String eyoneExternalId;
    private Boolean disable;
    private String startDate;
    private String endDate;
    private String carePeriodAsStr;
    private InsurerDto insurer;
    private String insurerNumber;
    private Integer carePercentage;
    private Integer careLimit;
    private Boolean hasAParticipant;
    private Boolean oneTimeUsage;
    private String beneficiaryEyoneExternalId;
    private String policeExternalId;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InsurerDto {
        private Integer identifier;
        private Boolean disable;
        private String name;
        private Integer insurerId;
        private Boolean activateTeletransmission;
    }
}
