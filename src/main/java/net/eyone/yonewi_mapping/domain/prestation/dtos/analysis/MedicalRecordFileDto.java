package net.eyone.yonewi_mapping.domain.prestation.dtos.analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordFileDto {
    private Integer identifier;
    private Boolean hta;
    private Boolean smoke;
    private Boolean cop;
    private Boolean makeSport;
    private Boolean cholesterol;
    private Boolean stress;
    private Boolean diabete;
    private Boolean heredity;
    private Boolean excessWeight;
    private Boolean asthma;
    private Boolean inactivity;
    private Boolean saos;
    private Boolean preeclampsie;
    private Boolean doMapa;
    private Boolean doEcg;
    private Boolean doBiologie;
    private Boolean doHolterEcg;
    private Boolean doECHOCARDIOGRAPHIE;
    private Double imc;
    private Integer relatedMedicalSellPrestationId;
    private Boolean hasGeneralExam;
    private Boolean hasCardioVascularyExam;
    private Boolean hasBreathingExam;
    private Boolean hasDigestifExam;
    private Boolean hasUrologyExam;
    private Boolean hasGynecologyExam;
    private Boolean hasSplenoGanglionaireExam;
    private Boolean hasLocomoteurExam;
    private Boolean hasNeurologicalExam;
    private Boolean hasBuccodentaireExam;
    private Boolean hasDermatoExam;
    private Boolean hasPilgrimFile;
    private Boolean hasGenitalExam;
    private Boolean referTo;
}
