package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalActCategoryDto {
    private String code;
    private String label;
    private Boolean evacuation;
    private Boolean modeleItem;
    private Boolean medicalExam;
    private Boolean pharmacyAsPrestation;
    private Boolean hospitalization;
    private Boolean analysis;
    private Boolean radiology;
    private Boolean quote;
    private Boolean all;
    private Boolean editable;
    private Boolean room;
    private Boolean medoc;
    private Boolean modele;
    private Boolean visit;
    private Boolean ambulatory;
    private Boolean consultation;
}
