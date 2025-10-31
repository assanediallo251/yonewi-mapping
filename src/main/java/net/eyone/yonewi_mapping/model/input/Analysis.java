package net.eyone.yonewi_mapping.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Analysis {
    private String messageType;
    private String messageKey;
    private String organismId;
    private String organismName;
    private String action;
    private String createdBy;
    private String creationDate;
    private AnalysisData data;
    private String origin;
    private String status;
    private Claims claims;

    @Data
    public static class AnalysisData {
        private Long identifier;
        private String eyoneInternalId;
        private String customId;
        private String createdBy;
        private String creationDate;
        private Organism organism;
        private Organism relatedService;
        private MedicalActCategory medicalActCategory;
        private Status status;
        private Patient patient;
        private String startDate;
        private String endDate;
        private PharmacySellPrestation pharmacySellPrestation;
        private Boolean toothPrestation;
        private Editor editor;
        private Doctor doctor;
        private MedicalRecordFile medicalRecordFile;
        private Integer toothIndice;
        private Boolean firstMedicalSellPrestation;
        private Boolean firstConsultation;
        private Boolean telePrestation;
        private Boolean createByQueue;
        private String documentIssuerStatus;
    }

    @Data
    public static class Organism {
        private Long identifier;
        private String name;
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
        private Boolean notPerformed;
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
    public static class PharmacySellPrestation {
        private Long identifier;
        private Boolean disable;
        private Integer nbrProducts;
        private String commentary;
        private PharmacyBillingMode pharmacyBillingMode;
        private Boolean usedAsAPrestation;
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
    public static class Editor {
        private Organism organism;
        private Long userId;
        private String userName;
        private String completeName;
        private UserDetailsInfos userDetailsInfos;
        private Long identifier;
        private Boolean doctor;
        private String completeNameAndUserName;
    }

    @Data
    public static class UserDetailsInfos {
        private Boolean hasSuperviseurRight;
        private Boolean doctor;
    }

    @Data
    public static class Doctor {
        private Long identifier;
        private Long doctorId;
        private String doctorUsername;
        private String doctorCompleteName;
        private Boolean ordonanceByDoctor;
        private Boolean facturedByDoctor;
    }

    @Data
    public static class MedicalRecordFile {
        private Long identifier;
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
        private Long relatedMedicalSellPrestationId;
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

    @Data
    public static class Claims {
        private String endpoint;
        private String method;
        @JsonProperty("class")
        private String className;
    }
}
