package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientBillingDto {
    private Integer identifier;
    private String creationDate;
    private Double payedAmountTTC;
    private Double notPayedAmountTTC;
    private Double careTotalAmount;
    private Integer careTotalPayedAmount;
    private Integer careTotalRejectAmount;
    private Double patientTotalAmount;
    private Integer patientTotalPayedAmount;
    private Integer complementaryCareTotalAmount;
    private Integer complementaryCareTotalPayedAmount;
    private Integer complementaryCareTotalRejectAmount;
    private Integer complementaryPatientTotalAmount;
    private Integer complementaryPatientTotalPayedAmount;
    private Integer nbrRejects;
    private Integer cautionTotalAmount;
    private Integer cautionOnGoingAmount;
    private Integer nbrPatientSchedules;
    private Integer nbrCareSchedules;
    private Integer returnToPatientAmount;
}
