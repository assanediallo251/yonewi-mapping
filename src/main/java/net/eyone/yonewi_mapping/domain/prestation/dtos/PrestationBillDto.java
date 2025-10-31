package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrestationBillDto {
    private Integer identifier;
    private String eyoneExternalId;
    private Boolean disable;
    private String name;
    private Integer itemSize;
    private Integer nbrItems;
    private String extension;
    private Boolean editable;
    private String path;
    private Boolean sentToMedicalPassport;
    private Boolean sentToInsurer;
    private MedicalDocumentTypeDto medicalDocumentType;
    private DocumentTypeDto documentType;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MedicalDocumentTypeDto {
        private String code;
        private String label;
        private Boolean medical;
        private Boolean financial;
        private Boolean bill;
        private Boolean prescription;
        private Boolean actPrescription;
        private Boolean medicalQuote;
        private Boolean examResult;
        private Boolean otherMedicalDocument;
        private Boolean scannedDocument;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DocumentTypeDto {
        private String code;
        private String label;
        private Boolean medical;
        private Boolean financial;
        private Boolean bill;
        private Boolean prescription;
        private Boolean actPrescription;
        private Boolean medicalQuote;
        private Boolean examResult;
        private Boolean otherMedicalDocument;
        private Boolean scannedDocument;
    }
}
