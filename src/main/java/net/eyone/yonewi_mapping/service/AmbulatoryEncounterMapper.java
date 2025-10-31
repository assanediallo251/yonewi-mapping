package net.eyone.yonewi_mapping.service;

import lombok.extern.slf4j.Slf4j;
import net.eyone.yonewi_mapping.model.input.Ambulatory;
import net.eyone.yonewi_mapping.model.output.EncounterR4;
import net.eyone.yonewi_mapping.model.output.EncounterR5;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public final class AmbulatoryEncounterMapper {

    private AmbulatoryEncounterMapper() {}

    // Constants
    private static final String RESOURCE_TYPE_ENCOUNTER = "Encounter";
    private static final String META_VERSION = "1";
    private static final String PROFILE_ENCOUNTER_AMB = "http://eyone.sn/fhir/StructureDefinition/Encounter-Ambulatory";
    private static final String SYS_IDENTIFIER_PRESTATION = "http://eyone.sn/fhir/identifier/prestation";
    private static final String SYS_IDENTIFIER_EYONE_INTERNAL = "http://eyone.sn/fhir/identifier/eyoneInternalId";
    private static final String SYS_IDENTIFIER_CUSTOM = "http://eyone.sn/fhir/identifier/customId";
    private static final String SYS_IDENTIFIER_ORGANISM = "http://eyone.sn/fhir/identifier/organism";
    private static final String SYS_MEDICAL_ACT_CATEGORY = "http://eyone.sn/fhir/CodeSystem/medical-act-category";
    private static final String SYS_SERVICE_DENOMINATION = "http://eyone.sn/fhir/CodeSystem/service-denomination";
    private static final String SYS_V3_ACTCODE = "http://terminology.hl7.org/CodeSystem/v3-ActCode";

    public static EncounterR4 toR4(Ambulatory in) {
        log.info("[AmbulatoryEncounterMapper] [toR4] input : {} , valeur", safeToString(in));
        try {
            if (in == null) return null;
            EncounterR4 out = new EncounterR4();
            out.resourceType = RESOURCE_TYPE_ENCOUNTER; log.info("[AmbulatoryEncounterMapper] [toR4] out.resourceType : {} , valeur", safeToString(out.resourceType));
            if (in.getData() != null) {
                out.id = safe(in.getData().getEyoneInternalId()); log.info("[AmbulatoryEncounterMapper] [toR4] out.id : {} , valeur", safeToString(out.id));
            }

            // meta
            EncounterR4.Meta meta = new EncounterR4.Meta(); log.info("[AmbulatoryEncounterMapper] [toR4] meta(new) : {} , valeur", safeToString(meta));
            meta.versionId = META_VERSION; log.info("[AmbulatoryEncounterMapper] [toR4] meta.versionId : {} , valeur", safeToString(meta.versionId));
            meta.lastUpdated = DateUtil.toIsoDateTime(safe(in.getData() != null ? in.getData().getCreationDate() : null)); log.info("[AmbulatoryEncounterMapper] [toR4] meta.lastUpdated : {} , valeur", safeToString(meta.lastUpdated));
            meta.profile = List.of(PROFILE_ENCOUNTER_AMB); log.info("[AmbulatoryEncounterMapper] [toR4] meta.profile[0] : {} , valeur", PROFILE_ENCOUNTER_AMB);
            out.meta = meta; log.info("[AmbulatoryEncounterMapper] [toR4] out.meta : {} , valeur", safeToString(out.meta));

            // identifiers
            out.identifier = new ArrayList<>(); log.info("[AmbulatoryEncounterMapper] [toR4] out.identifier(new) : {} , valeur", safeToString(out.identifier));
            if (in.getData() != null) {
                addIdentifierR4(out, SYS_IDENTIFIER_PRESTATION, stringOrNull(in.getData().getIdentifier()));
                addIdentifierR4(out, SYS_IDENTIFIER_EYONE_INTERNAL, in.getData().getEyoneInternalId());
                addIdentifierR4(out, SYS_IDENTIFIER_CUSTOM, in.getData().getCustomId());
            }
            addIdentifierR4(out, SYS_IDENTIFIER_ORGANISM, in.getOrganismId());

            // status
            out.status = mapStatusR4(code(in.getData() != null && in.getData().getStatus() != null ? in.getData().getStatus().getCode() : null)); log.info("[AmbulatoryEncounterMapper] [toR4] out.status : {} , valeur", safeToString(out.status));
            // statusHistory (basic)
            if (in.getData() != null && (in.getData().getStartDate() != null || in.getData().getEndDate() != null)) {
                EncounterR4.StatusHistory sh = new EncounterR4.StatusHistory(); log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory(new) : {} , valeur", safeToString(sh));
                sh.status = out.status; log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory[0].status : {} , valeur", safeToString(sh.status));
                EncounterR4.Period p = new EncounterR4.Period(); log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory[0].period(new) : {} , valeur", safeToString(p));
                p.start = DateUtil.toIsoDateTime(in.getData().getStartDate()); log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory[0].period.start : {} , valeur", safeToString(p.start));
                p.end = DateUtil.toIsoDateTime(in.getData().getEndDate()); log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory[0].period.end : {} , valeur", safeToString(p.end));
                sh.period = p;
                out.statusHistory = List.of(sh); log.info("[AmbulatoryEncounterMapper] [toR4] out.statusHistory.size : {} , valeur", 1);
            }

            // class
            EncounterR4.EncounterClass ec = new EncounterR4.EncounterClass(); log.info("[AmbulatoryEncounterMapper] [toR4] class(new) : {} , valeur", safeToString(ec));
            EncounterClassTriple triple = mapEncounterClass(code(in.getData() != null && in.getData().getMedicalActCategory() != null ? in.getData().getMedicalActCategory().getCode() : null));
            ec.system = triple.system; log.info("[AmbulatoryEncounterMapper] [toR4] class.system : {} , valeur", safeToString(ec.system));
            ec.code = triple.code; log.info("[AmbulatoryEncounterMapper] [toR4] class.code : {} , valeur", safeToString(ec.code));
            ec.display = triple.display; log.info("[AmbulatoryEncounterMapper] [toR4] class.display : {} , valeur", safeToString(ec.display));
            out.encounterClass = ec; log.info("[AmbulatoryEncounterMapper] [toR4] out.class : {} , valeur", safeToString(out.encounterClass));

            // type
            if (in.getData() != null && in.getData().getMedicalActCategory() != null) {
                EncounterR4.Coding coding = new EncounterR4.Coding(); log.info("[AmbulatoryEncounterMapper] [toR4] type.coding(new) : {} , valeur", safeToString(coding));
                coding.code = in.getData().getMedicalActCategory().getCode(); log.info("[AmbulatoryEncounterMapper] [toR4] type[0].coding[0].code : {} , valeur", safeToString(coding.code));
                coding.display = in.getData().getMedicalActCategory().getLabel(); log.info("[AmbulatoryEncounterMapper] [toR4] type[0].coding[0].display : {} , valeur", safeToString(coding.display));
                coding.system = SYS_MEDICAL_ACT_CATEGORY; log.info("[AmbulatoryEncounterMapper] [toR4] type[0].coding[0].system : {} , valeur", safeToString(coding.system));
                EncounterR4.CodeableConcept cc = new EncounterR4.CodeableConcept();
                cc.coding = List.of(coding);
                out.type = List.of(cc); log.info("[AmbulatoryEncounterMapper] [toR4] out.type.size : {} , valeur", 1);
            }

            // serviceType
            if (in.getData() != null && in.getData().getDenomination() != null) {
                String den = in.getData().getDenomination(); log.info("[AmbulatoryEncounterMapper] [toR4] denomination(in) : {} , valeur", safeToString(den));
                EncounterR4.Coding coding = new EncounterR4.Coding(); log.info("[AmbulatoryEncounterMapper] [toR4] serviceType.coding(new) : {} , valeur", safeToString(coding));
                coding.code = normalizeCode(den); log.info("[AmbulatoryEncounterMapper] [toR4] serviceType.coding.code : {} , valeur", safeToString(coding.code));
                coding.display = den; log.info("[AmbulatoryEncounterMapper] [toR4] serviceType.coding.display : {} , valeur", safeToString(coding.display));
                coding.system = SYS_SERVICE_DENOMINATION; log.info("[AmbulatoryEncounterMapper] [toR4] serviceType.coding.system : {} , valeur", safeToString(coding.system));
                EncounterR4.CodeableConcept cc = new EncounterR4.CodeableConcept();
                cc.coding = List.of(coding);
                out.serviceType = cc; log.info("[AmbulatoryEncounterMapper] [toR4] out.serviceType : {} , valeur", safeToString(out.serviceType));
            }

            // subject (patient)
            if (in.getData() != null && in.getData().getPatient() != null) {
                EncounterR4.Reference ref = new EncounterR4.Reference(); log.info("[AmbulatoryEncounterMapper] [toR4] subject(new) : {} , valeur", safeToString(ref));
                String eyId = in.getData().getPatient().getEyoneInternalId(); log.info("[AmbulatoryEncounterMapper] [toR4] patient.eyoneInternalId : {} , valeur", safeToString(eyId));
                if (eyId != null) {
                    ref.reference = "Patient/" + eyId; log.info("[AmbulatoryEncounterMapper] [toR4] subject.reference : {} , valeur", safeToString(ref.reference));
                    ref.display = buildPatientDisplay(in.getData().getPatient().getFirstName(), in.getData().getPatient().getLastName()); log.info("[AmbulatoryEncounterMapper] [toR4] subject.display : {} , valeur", safeToString(ref.display));
                } else if (in.getData().getPatient().getIdentifier() != null) {
                    ref.reference = "Patient/" + in.getData().getPatient().getIdentifier(); log.info("[AmbulatoryEncounterMapper] [toR4] subject.reference : {} , valeur", safeToString(ref.reference));
                }
                out.subject = ref; log.info("[AmbulatoryEncounterMapper] [toR4] out.subject : {} , valeur", safeToString(out.subject));
            }

            // period
            if (in.getData() != null && (in.getData().getStartDate() != null || in.getData().getEndDate() != null)) {
                EncounterR4.Period p = new EncounterR4.Period(); log.info("[AmbulatoryEncounterMapper] [toR4] period(new) : {} , valeur", safeToString(p));
                p.start = DateUtil.toIsoDateTime(in.getData().getStartDate()); log.info("[AmbulatoryEncounterMapper] [toR4] period.start : {} , valeur", safeToString(p.start));
                p.end = DateUtil.toIsoDateTime(in.getData().getEndDate()); log.info("[AmbulatoryEncounterMapper] [toR4] period.end : {} , valeur", safeToString(p.end));
                out.period = p; log.info("[AmbulatoryEncounterMapper] [toR4] out.period : {} , valeur", safeToString(out.period));
            }

            // serviceProvider (organism)
            if (in.getData() != null && in.getData().getOrganism() != null) {
                EncounterR4.Reference ref = new EncounterR4.Reference(); log.info("[AmbulatoryEncounterMapper] [toR4] serviceProvider(new) : {} , valeur", safeToString(ref));
                Long id = in.getData().getOrganism().getIdentifier(); log.info("[AmbulatoryEncounterMapper] [toR4] organism.identifier : {} , valeur", safeToString(id));
                String name = in.getData().getOrganism().getName(); log.info("[AmbulatoryEncounterMapper] [toR4] organism.name : {} , valeur", safeToString(name));
                if (id != null) ref.reference = "Organization/" + id; log.info("[AmbulatoryEncounterMapper] [toR4] serviceProvider.reference : {} , valeur", safeToString(ref.reference));
                ref.display = name; log.info("[AmbulatoryEncounterMapper] [toR4] serviceProvider.display : {} , valeur", safeToString(ref.display));
                out.serviceProvider = ref; log.info("[AmbulatoryEncounterMapper] [toR4] out.serviceProvider : {} , valeur", safeToString(out.serviceProvider));
            }

            log.info("[AmbulatoryEncounterMapper] [toR4] output : {} , valeur", safeToString(out));
            return out;
        } catch (Exception e) {
            log.info("[AmbulatoryEncounterMapper] [toR4] exception : {} , valeur", e.toString(), e);
            throw new RuntimeException("AmbulatoryEncounterMapper.toR4 failed", e);
        }
    }

    public static EncounterR5 toR5(Ambulatory in) {
        log.info("[AmbulatoryEncounterMapper] [toR5] input : {} , valeur", safeToString(in));
        try {
            if (in == null) return null;
            EncounterR5 out = new EncounterR5();
            out.resourceType = RESOURCE_TYPE_ENCOUNTER; log.info("[AmbulatoryEncounterMapper] [toR5] out.resourceType : {} , valeur", safeToString(out.resourceType));
            if (in.getData() != null) {
                out.id = safe(in.getData().getEyoneInternalId()); log.info("[AmbulatoryEncounterMapper] [toR5] out.id : {} , valeur", safeToString(out.id));
            }

            // meta
            EncounterR5.Meta meta = new EncounterR5.Meta(); log.info("[AmbulatoryEncounterMapper] [toR5] meta(new) : {} , valeur", safeToString(meta));
            meta.versionId = META_VERSION; log.info("[AmbulatoryEncounterMapper] [toR5] meta.versionId : {} , valeur", safeToString(meta.versionId));
            meta.lastUpdated = DateUtil.toIsoDateTime(safe(in.getData() != null ? in.getData().getCreationDate() : null)); log.info("[AmbulatoryEncounterMapper] [toR5] meta.lastUpdated : {} , valeur", safeToString(meta.lastUpdated));
            meta.profile = List.of(PROFILE_ENCOUNTER_AMB); log.info("[AmbulatoryEncounterMapper] [toR5] meta.profile[0] : {} , valeur", PROFILE_ENCOUNTER_AMB);
            out.meta = meta; log.info("[AmbulatoryEncounterMapper] [toR5] out.meta : {} , valeur", safeToString(out.meta));

            // identifiers
            out.identifier = new ArrayList<>(); log.info("[AmbulatoryEncounterMapper] [toR5] out.identifier(new) : {} , valeur", safeToString(out.identifier));
            if (in.getData() != null) {
                addIdentifierR5(out, SYS_IDENTIFIER_PRESTATION, stringOrNull(in.getData().getIdentifier()));
                addIdentifierR5(out, SYS_IDENTIFIER_EYONE_INTERNAL, in.getData().getEyoneInternalId());
                addIdentifierR5(out, SYS_IDENTIFIER_CUSTOM, in.getData().getCustomId());
            }
            addIdentifierR5(out, SYS_IDENTIFIER_ORGANISM, in.getOrganismId());

            // status
            out.status = mapStatusR4(code(in.getData() != null && in.getData().getStatus() != null ? in.getData().getStatus().getCode() : null)); log.info("[AmbulatoryEncounterMapper] [toR5] out.status : {} , valeur", safeToString(out.status));

            // class (R5 list)
            EncounterR5.EncounterClass ec = new EncounterR5.EncounterClass(); log.info("[AmbulatoryEncounterMapper] [toR5] class(new) : {} , valeur", safeToString(ec));
            EncounterClassTriple triple = mapEncounterClass(code(in.getData() != null && in.getData().getMedicalActCategory() != null ? in.getData().getMedicalActCategory().getCode() : null));
            EncounterR5.Coding coding = new EncounterR5.Coding(); log.info("[AmbulatoryEncounterMapper] [toR5] class.coding(new) : {} , valeur", safeToString(coding));
            coding.system = triple.system; log.info("[AmbulatoryEncounterMapper] [toR5] class.coding.system : {} , valeur", safeToString(coding.system));
            coding.code = triple.code; log.info("[AmbulatoryEncounterMapper] [toR5] class.coding.code : {} , valeur", safeToString(coding.code));
            coding.display = triple.display; log.info("[AmbulatoryEncounterMapper] [toR5] class.coding.display : {} , valeur", safeToString(coding.display));
            ec.coding = List.of(coding);
            out.encounterClass = List.of(ec); log.info("[AmbulatoryEncounterMapper] [toR5] out.class.size : {} , valeur", 1);

            // type
            if (in.getData() != null && in.getData().getMedicalActCategory() != null) {
                EncounterR5.Coding typCoding = new EncounterR5.Coding(); log.info("[AmbulatoryEncounterMapper] [toR5] type.coding(new) : {} , valeur", safeToString(typCoding));
                typCoding.code = in.getData().getMedicalActCategory().getCode(); log.info("[AmbulatoryEncounterMapper] [toR5] type[0].coding[0].code : {} , valeur", safeToString(typCoding.code));
                typCoding.display = in.getData().getMedicalActCategory().getLabel(); log.info("[AmbulatoryEncounterMapper] [toR5] type[0].coding[0].display : {} , valeur", safeToString(typCoding.display));
                typCoding.system = SYS_MEDICAL_ACT_CATEGORY; log.info("[AmbulatoryEncounterMapper] [toR5] type[0].coding[0].system : {} , valeur", safeToString(typCoding.system));
                EncounterR5.CodeableConcept cc = new EncounterR5.CodeableConcept();
                cc.coding = List.of(typCoding);
                out.type = List.of(cc); log.info("[AmbulatoryEncounterMapper] [toR5] out.type.size : {} , valeur", 1);
            }

            // serviceType (list of ServiceTypeItem with Concept)
            if (in.getData() != null && in.getData().getDenomination() != null) {
                String den = in.getData().getDenomination(); log.info("[AmbulatoryEncounterMapper] [toR5] denomination(in) : {} , valeur", safeToString(den));
                EncounterR5.Coding svCoding = new EncounterR5.Coding(); log.info("[AmbulatoryEncounterMapper] [toR5] serviceType.coding(new) : {} , valeur", safeToString(svCoding));
                svCoding.code = normalizeCode(den); log.info("[AmbulatoryEncounterMapper] [toR5] serviceType.coding.code : {} , valeur", safeToString(svCoding.code));
                svCoding.display = den; log.info("[AmbulatoryEncounterMapper] [toR5] serviceType.coding.display : {} , valeur", safeToString(svCoding.display));
                svCoding.system = SYS_SERVICE_DENOMINATION; log.info("[AmbulatoryEncounterMapper] [toR5] serviceType.coding.system : {} , valeur", safeToString(svCoding.system));
                EncounterR5.Concept concept = new EncounterR5.Concept();
                concept.coding = List.of(svCoding);
                EncounterR5.ServiceTypeItem item = new EncounterR5.ServiceTypeItem();
                item.concept = concept;
                out.serviceType = List.of(item); log.info("[AmbulatoryEncounterMapper] [toR5] out.serviceType.size : {} , valeur", 1);
            }

            // subject
            if (in.getData() != null && in.getData().getPatient() != null) {
                EncounterR5.Reference ref = new EncounterR5.Reference(); log.info("[AmbulatoryEncounterMapper] [toR5] subject(new) : {} , valeur", safeToString(ref));
                String eyId = in.getData().getPatient().getEyoneInternalId(); log.info("[AmbulatoryEncounterMapper] [toR5] patient.eyoneInternalId : {} , valeur", safeToString(eyId));
                if (eyId != null) {
                    ref.reference = "Patient/" + eyId; log.info("[AmbulatoryEncounterMapper] [toR5] subject.reference : {} , valeur", safeToString(ref.reference));
                    ref.display = buildPatientDisplay(in.getData().getPatient().getFirstName(), in.getData().getPatient().getLastName()); log.info("[AmbulatoryEncounterMapper] [toR5] subject.display : {} , valeur", safeToString(ref.display));
                } else if (in.getData().getPatient().getIdentifier() != null) {
                    ref.reference = "Patient/" + in.getData().getPatient().getIdentifier(); log.info("[AmbulatoryEncounterMapper] [toR5] subject.reference : {} , valeur", safeToString(ref.reference));
                }
                out.subject = ref; log.info("[AmbulatoryEncounterMapper] [toR5] out.subject : {} , valeur", safeToString(out.subject));
            }

            // actualPeriod
            if (in.getData() != null && (in.getData().getStartDate() != null || in.getData().getEndDate() != null)) {
                EncounterR5.Period p = new EncounterR5.Period(); log.info("[AmbulatoryEncounterMapper] [toR5] actualPeriod(new) : {} , valeur", safeToString(p));
                p.start = DateUtil.toIsoDateTime(in.getData().getStartDate()); log.info("[AmbulatoryEncounterMapper] [toR5] actualPeriod.start : {} , valeur", safeToString(p.start));
                p.end = DateUtil.toIsoDateTime(in.getData().getEndDate()); log.info("[AmbulatoryEncounterMapper] [toR5] actualPeriod.end : {} , valeur", safeToString(p.end));
                out.actualPeriod = p; log.info("[AmbulatoryEncounterMapper] [toR5] out.actualPeriod : {} , valeur", safeToString(out.actualPeriod));
            }

            // serviceProvider
            if (in.getData() != null && in.getData().getOrganism() != null) {
                EncounterR5.Reference ref = new EncounterR5.Reference(); log.info("[AmbulatoryEncounterMapper] [toR5] serviceProvider(new) : {} , valeur", safeToString(ref));
                Long id = in.getData().getOrganism().getIdentifier(); log.info("[AmbulatoryEncounterMapper] [toR5] organism.identifier : {} , valeur", safeToString(id));
                String name = in.getData().getOrganism().getName(); log.info("[AmbulatoryEncounterMapper] [toR5] organism.name : {} , valeur", safeToString(name));
                if (id != null) ref.reference = "Organization/" + id; log.info("[AmbulatoryEncounterMapper] [toR5] serviceProvider.reference : {} , valeur", safeToString(ref.reference));
                ref.display = name; log.info("[AmbulatoryEncounterMapper] [toR5] serviceProvider.display : {} , valeur", safeToString(ref.display));
                out.serviceProvider = ref; log.info("[AmbulatoryEncounterMapper] [toR5] out.serviceProvider : {} , valeur", safeToString(out.serviceProvider));
            }

            log.info("[AmbulatoryEncounterMapper] [toR5] output : {} , valeur", safeToString(out));
            return out;
        } catch (Exception e) {
            log.info("[AmbulatoryEncounterMapper] [toR5] exception : {} , valeur", e.toString(), e);
            throw new RuntimeException("AmbulatoryEncounterMapper.toR5 failed", e);
        }
    }

    private static void addIdentifierR4(EncounterR4 out, String system, String value) {
        if (value == null || value.isBlank()) return;
        EncounterR4.Identifier id = new EncounterR4.Identifier(); log.info("[AmbulatoryEncounterMapper] [addIdentifierR4] identifier(new) : {} , valeur", safeToString(id));
        id.system = system; log.info("[AmbulatoryEncounterMapper] [addIdentifierR4] identifier.system : {} , valeur", safeToString(id.system));
        id.value = value; log.info("[AmbulatoryEncounterMapper] [addIdentifierR4] identifier.value : {} , valeur", safeToString(id.value));
        out.identifier.add(id); log.info("[AmbulatoryEncounterMapper] [addIdentifierR4] out.identifier.size : {} , valeur", out.identifier.size());
    }

    private static void addIdentifierR5(EncounterR5 out, String system, String value) {
        if (value == null || value.isBlank()) return;
        EncounterR5.Identifier id = new EncounterR5.Identifier(); log.info("[AmbulatoryEncounterMapper] [addIdentifierR5] identifier(new) : {} , valeur", safeToString(id));
        id.system = system; log.info("[AmbulatoryEncounterMapper] [addIdentifierR5] identifier.system : {} , valeur", safeToString(id.system));
        id.value = value; log.info("[AmbulatoryEncounterMapper] [addIdentifierR5] identifier.value : {} , valeur", safeToString(id.value));
        out.identifier.add(id); log.info("[AmbulatoryEncounterMapper] [addIdentifierR5] out.identifier.size : {} , valeur", out.identifier.size());
    }

    private static String mapStatusR4(String code) {
        if (code == null) return null;
        return switch (code) {
            case "ON_GOING" -> "in-progress";
            case "VALIDATED" -> "finished";
            case "SCHEDULED", "CREATED" -> "planned";
            case "CANCELLED" -> "cancelled";
            default -> "unknown";
        };
    }

    private static EncounterClassTriple mapEncounterClass(String siCode) {
        // Defaults to AMB
        String system = SYS_V3_ACTCODE;
        String code = "AMB";
        String display = "ambulatory";
        if (siCode != null) {
            switch (siCode) {
                case "AMBULATORY":
                case "CONSULTATION":
                    code = "AMB"; display = "ambulatory"; break;
                case "HOSPITALIZATION":
                    code = "IMP"; display = "inpatient encounter"; break;
                case "VISIT":
                    code = "HH"; display = "home health"; break;
                case "EMERGENCY":
                    code = "EMER"; display = "emergency"; break;
                default:
                    // keep defaults
                    break;
            }
        }
        return new EncounterClassTriple(system, code, display);
    }

    private static class EncounterClassTriple {
        public final String system; public final String code; public final String display;
        private EncounterClassTriple(String system, String code, String display) {
            this.system = system; this.code = code; this.display = display;
        }
    }

    private static String safe(String s) { return s == null ? null : s; }
    private static String stringOrNull(Object o) { return o == null ? null : Objects.toString(o); }
    private static String code(String s) { return s == null ? null : s.trim(); }

    private static String buildPatientDisplay(String first, String last) {
        String f = first == null ? "" : first;
        String l = last == null ? "" : last;
        String v = (f + " " + l).trim();
        return v.isEmpty() ? null : v;
    }

    private static String normalizeCode(String s) {
        if (s == null) return null;
        return s.trim().toUpperCase().replace(' ', '_');
    }

    private static String safeToString(Object value) {
        if (value == null) return "null";
        try { return String.valueOf(value); } catch (Exception e) { return value.getClass().getSimpleName(); }
    }
}
