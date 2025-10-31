package net.eyone.yonewi_mapping.domain.prestation.dtos.analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eyone.yonewi_mapping.domain.prestation.dtos.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDataDto {
    private Integer identifier;
    private String eyoneInternalId;
    private String customId;
    private String createdBy;
    private String creationDate;
    private OrganismDto organism;
    private relatedServiceDto relatedService;
    private MedicalActCategoryDto medicalActCategory;
    private StatusDto status;
    private PatientDto patient;
    private String startDate;
    private String endDate;
    private PharmacySellPrestationDto pharmacySellPrestation;
    private Boolean toothPrestation;
    private EditorDto editor;
    private DoctorDto doctor;
    private MedicalRecordFileDto medicalRecordFile;
    private Integer toothIndice;
    private Boolean firstMedicalSellPrestation;
    private Boolean firstConsultation;
    private Boolean telePrestation;
    private Boolean createByQueue;
    private String documentIssuerStatus;
}
