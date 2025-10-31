package net.eyone.yonewi_mapping.constant;

public final class MappingConstants {
    private MappingConstants() {}

    public static final String RESOURCE_TYPE_ENCOUNTER = "Encounter";
    public static final String META_VERSION = "1";
    public static final String PROFILE_ENCOUNTER_AMB = "http://eyone.sn/fhir/StructureDefinition/Encounter-Ambulatory";

    public static final String SYS_IDENTIFIER_PRESTATION = "http://eyone.sn/fhir/identifier/prestation";
    public static final String SYS_IDENTIFIER_EYONE_INTERNAL = "http://eyone.sn/fhir/identifier/eyoneInternalId";
    public static final String SYS_IDENTIFIER_CUSTOM = "http://eyone.sn/fhir/identifier/customId";
    public static final String SYS_IDENTIFIER_ORGANISM = "http://eyone.sn/fhir/identifier/organism";

    public static final String SYS_MEDICAL_ACT_CATEGORY = "http://eyone.sn/fhir/CodeSystem/medical-act-category";
    public static final String SYS_SERVICE_DENOMINATION = "http://eyone.sn/fhir/CodeSystem/service-denomination";
    public static final String SYS_V3_ACTCODE = "http://terminology.hl7.org/CodeSystem/v3-ActCode";

    // StructureDefinition URLs (shared)
    public static final String SD_MESSAGE_TYPE = "http://eyone.sn/fhir/StructureDefinition/messageType";
    public static final String SD_MESSAGE_KEY = "http://eyone.sn/fhir/StructureDefinition/messageKey";
    public static final String SD_ACTION = "http://eyone.sn/fhir/StructureDefinition/action";
    public static final String SD_ORIGIN = "http://eyone.sn/fhir/StructureDefinition/origin";
    public static final String SD_MESSAGE_STATUS = "http://eyone.sn/fhir/StructureDefinition/messageStatus";
    public static final String SD_CREATED_BY = "http://eyone.sn/fhir/StructureDefinition/createdBy";
    public static final String SD_CREATION_DATE = "http://eyone.sn/fhir/StructureDefinition/creationDate";
    public static final String SD_MEDICAL_ACT_CATEGORY = "http://eyone.sn/fhir/StructureDefinition/medicalActCategory";
    public static final String SD_STATUS_DETAILS = "http://eyone.sn/fhir/StructureDefinition/statusDetails";
    public static final String SD_PHARMACY_SELL_PRESTATION = "http://eyone.sn/fhir/StructureDefinition/pharmacySellPrestation";
    public static final String SD_TOOTH_PRESTATION = "http://eyone.sn/fhir/StructureDefinition/toothPrestation";
    public static final String SD_EDITOR = "http://eyone.sn/fhir/StructureDefinition/editor";
    public static final String SD_RELATED_SERVICE = "http://eyone.sn/fhir/StructureDefinition/relatedService";
    public static final String SD_CLAIMS = "http://eyone.sn/fhir/StructureDefinition/claims";

    // Additional R4-specific extensions seen in sample
    public static final String SD_DOCTOR = "http://eyone.sn/fhir/StructureDefinition/doctor";
    public static final String SD_MEDICAL_RECORD_FILE = "http://eyone.sn/fhir/StructureDefinition/medicalRecordFile";
    public static final String SD_FIRST_MEDICAL_SELL_PRESTATION = "http://eyone.sn/fhir/StructureDefinition/firstMedicalSellPrestation";
    public static final String SD_FIRST_CONSULTATION = "http://eyone.sn/fhir/StructureDefinition/firstConsultation";
    public static final String SD_TELE_PRESTATION = "http://eyone.sn/fhir/StructureDefinition/telePrestation";
    public static final String SD_CREATE_BY_QUEUE = "http://eyone.sn/fhir/StructureDefinition/createByQueue";
    public static final String SD_DOCUMENT_ISSUER_STATUS = "http://eyone.sn/fhir/StructureDefinition/documentIssuerStatus";
    public static final String SD_TOOTH_INDICE = "http://eyone.sn/fhir/StructureDefinition/toothIndice";

    // Claims endpoint
    public static final String CLAIMS_ENDPOINT_MEDICAL_WORKFLOW_STATUS = "/medical-workflow-status";
}
