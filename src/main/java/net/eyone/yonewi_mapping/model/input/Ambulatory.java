package net.eyone.yonewi_mapping.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Ambulatory {
    private String messageType;
    private String organismId;
    private String organismName;
    private String action;
    private AmbulatoryData data;
    private String origin;
    private Claims claims;

    @Data
    public static class AmbulatoryData {
        private Long identifier;
        private String eyoneInternalId;
        private String customId;
        private String createdBy;
        private String creationDate;
        private Organism organism;
        private Organism relatedService;
        private Integer price;
        private String denomination;
        private MedicalActCategory medicalActCategory;
        private Status status;
        private PrestationPaymentStatus prestationPaymentStatus;
        private ProgrammationType programmationType;
        private Patient patient;
        private String startDate;
        private String endDate;
        private PatientBilling patientBilling;
        private PrestationBill prestationBill;
        private Document[] documents;
        private PharmacySellPrestation pharmacySellPrestation;
        private Care care;
        private Boolean toothPrestation;
        private Boolean indicateThatPatientHasCare;
        private Boolean hasRelatedMedicalQuote;
        private Integer queueOrderNumber;
        private Integer totalReductions;
        private String linkRelatedMedicalPartDate;
        private Integer nbDay;
        private Boolean patientSelf;
        private Integer totalPharmaciePrice;
        private Integer totalMedicalActPrice;
        private Integer totalPharmacyPriceWithoutAct;
        private Integer totalRadiologiePrice;
        private Integer totalAnalysisPrice;
        private Integer totalPrice;
        private Integer itemsTotalPrice;
        private Boolean documentGenerationRequired;
        private Boolean documentGenerationRequired2;
        private Boolean selectedPayPatientQuotePart;
        private Editor editor;
    }

    @Data
    public static class Organism {
        private Long identifier;
        private String name;
        private Boolean billingInsuranceAutoSynchronization;
        private String eyoneExternalId;
    }

    @Data
    public static class MedicalActCategory {
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

    @Data
    public static class Status {
        private String code;
        private String label;
        private Boolean done;
        private Boolean created;
        private Boolean cancelled;
        private Boolean toBeConfirmed;
        private Boolean notAccepted;
        private Boolean onGoing;
        private Boolean unavailable;
        private Boolean waiting;
        private Boolean notPerformed;
    }

    @Data
    public static class PrestationPaymentStatus {
        private String code;
        private String label;
        private Boolean totallyPayed;
        private Boolean paying;
        private Boolean notPayed;
        private Boolean payedWithReject;
    }

    @Data
    public static class ProgrammationType {
        private String code;
        private String label;
        private Boolean notScheduled;
        private Boolean emergency;
        private Boolean scheduled;
    }

    @Data
    public static class Patient {
        private Long identifier;
        private String eyoneInternalId;
        private String firstName;
        private String lastName;
        private Sex sex;
    }

    @Data
    public static class Sex {
        private String code;
        private String label;
    }

    @Data
    public static class PatientBilling {
        private Long identifier;
        private String creationDate;
        private Integer payedAmountTTC;
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

    @Data
    public static class PrestationBill {
        private Long identifier;
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
        private DocumentType medicalDocumentType;
        private DocumentType documentType;
    }

    @Data
    public static class DocumentType {
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

    @Data
    public static class Document {
        private String url;
        private String originalUrl;
        private String bucketName;
        private Integer sizeBytes;
        private String fileType;
        private String fileName;
        private String documentType;
        private String documentTypeCode;
        private Long fileId;
        private Long entityId;
        private String eyoneExternalId;
    }

    @Data
    public static class PharmacySellPrestation {
        private Long identifier;
        private Boolean disable;
        private Integer totalPrice;
        private Integer nbrProducts;
        private String commentary;
        private PharmacyBillingMode pharmacyBillingMode;
        private Care care;
        private Boolean usedAsAPrestation;
        private Boolean useComplementaryCare;
        private Boolean manageStock;
        private Boolean documentGenerationRequired2;
    }

    @Data
    public static class PharmacyBillingMode {
        private String code;
        private String label;
        private String scope;
        private Boolean global;
        private Boolean detail;
    }

    @Data
    public static class Care {
        private Long identifier;
        private String eyoneExternalId;
        private Boolean disable;
        private String startDate;
        private String endDate;
        private String carePeriodAsStr;
        private Insurer insurer;
        private String insurerNumber;
        private Integer carePercentage;
        private Integer careLimit;
        private Boolean hasAParticipant;
        private Boolean oneTimeUsage;
        private String beneficiaryEyoneExternalId;
        private String policeExternalId;
    }

    @Data
    public static class Insurer {
        private Long identifier;
        private Boolean disable;
        private String name;
        private Long insurerId;
        private Boolean activateTeletransmission;
    }

    @Data
    public static class Editor {
        private Organism organism;
        private Long userId;
        private String userName;
        private String completeName;
        private UserDetailsInfos userDetailsInfos;
        private Long identifier;
        private String completeNameAndUserName;
    }

    @Data
    public static class UserDetailsInfos {
        private Boolean hasSuperviseurRight;
        private Boolean doctor;
    }

    @Data
    public static class Claims {
        private String method;
        @JsonProperty("class")
        private String className;
    }
}
