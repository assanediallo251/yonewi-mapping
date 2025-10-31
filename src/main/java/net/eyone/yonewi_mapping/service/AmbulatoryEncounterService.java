package net.eyone.yonewi_mapping.service;

import lombok.extern.slf4j.Slf4j;
import net.eyone.yonewi_mapping.model.input.Ambulatory;
import net.eyone.yonewi_mapping.model.output.EncounterR4;
import net.eyone.yonewi_mapping.model.output.EncounterR5;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.eyone.yonewi_mapping.constant.MappingConstants;

@Slf4j
public final class AmbulatoryEncounterService {

    private AmbulatoryEncounterService() {
    }

    public static EncounterR4 toR4(Ambulatory in) {
        log.info("[AmbulatoryEncounterMapper] [toR4] input : {} , valeur", safeToString(in));
        try {
            if (in == null)
                return null;
            EncounterR4 out = new EncounterR4();
            out.resourceType = MappingConstants.RESOURCE_TYPE_ENCOUNTER;
            log.info("[AmbulatoryEncounterMapper] [toR4] out.resourceType : {} , valeur",
                    safeToString(out.resourceType));
            if (in.getData() != null) {
                out.id = safe(in.getData().getEyoneInternalId());
                log.info("[AmbulatoryEncounterMapper] [toR4] out.id : {} , valeur", safeToString(out.id));
            }

            // meta
            EncounterR4.Meta meta = new EncounterR4.Meta();
            log.info("[AmbulatoryEncounterMapper] [toR4] meta(new) : {} , valeur", safeToString(meta));
            meta.versionId = MappingConstants.META_VERSION;
            log.info("[AmbulatoryEncounterMapper] [toR4] meta.versionId : {} , valeur", safeToString(meta.versionId));
            meta.lastUpdated = DateUtil
                    .toIsoDateTime(safe(in.getData() != null ? in.getData().getCreationDate() : null));
            log.info("[AmbulatoryEncounterMapper] [toR4] meta.lastUpdated : {} , valeur",
                    safeToString(meta.lastUpdated));
            meta.profile = List.of(MappingConstants.PROFILE_ENCOUNTER_AMB);
            log.info("[AmbulatoryEncounterMapper] [toR4] meta.profile[0] : {} , valeur",
                    MappingConstants.PROFILE_ENCOUNTER_AMB);
            out.meta = meta;
            log.info("[AmbulatoryEncounterMapper] [toR4] out.meta : {} , valeur", safeToString(out.meta));

            // identifiers
            out.identifier = new ArrayList<>();
            log.info("[AmbulatoryEncounterMapper] [toR4] out.identifier(new) : {} , valeur",
                    safeToString(out.identifier));
            if (in.getData() != null) {
                addIdentifierR4(out, MappingConstants.SYS_IDENTIFIER_PRESTATION,
                        stringOrNull(in.getData().getIdentifier()));
                addIdentifierR4(out, MappingConstants.SYS_IDENTIFIER_EYONE_INTERNAL, in.getData().getEyoneInternalId());
                addIdentifierR4(out, MappingConstants.SYS_IDENTIFIER_CUSTOM, in.getData().getCustomId());
            }
            addIdentifierR4(out, MappingConstants.SYS_IDENTIFIER_ORGANISM, in.getOrganismId());

            // status
            out.status = mapStatusR4(
                    code(in.getData() != null && in.getData().getStatus() != null ? in.getData().getStatus().getCode()
                            : null));
            log.info("[AmbulatoryEncounterMapper] [toR4] out.status : {} , valeur", safeToString(out.status));
            // statusHistory (basic)
            if (in.getData() != null && (in.getData().getStartDate() != null || in.getData().getEndDate() != null)) {
                EncounterR4.StatusHistory sh = new EncounterR4.StatusHistory();
                log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory(new) : {} , valeur", safeToString(sh));
                sh.status = out.status;
                log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory[0].status : {} , valeur",
                        safeToString(sh.status));
                EncounterR4.Period p = new EncounterR4.Period();
                log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory[0].period(new) : {} , valeur",
                        safeToString(p));
                p.start = DateUtil.toIsoDateTime(in.getData().getStartDate());
                log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory[0].period.start : {} , valeur",
                        safeToString(p.start));
                p.end = DateUtil.toIsoDateTime(in.getData().getEndDate());
                log.info("[AmbulatoryEncounterMapper] [toR4] statusHistory[0].period.end : {} , valeur",
                        safeToString(p.end));
                sh.period = p;
                out.statusHistory = List.of(sh);
                log.info("[AmbulatoryEncounterMapper] [toR4] out.statusHistory.size : {} , valeur", 1);
            }

            // class
            EncounterR4.EncounterClass ec = new EncounterR4.EncounterClass();
            log.info("[AmbulatoryEncounterMapper] [toR4] class(new) : {} , valeur", safeToString(ec));
            EncounterClassTriple triple = mapEncounterClass(
                    code(in.getData() != null && in.getData().getMedicalActCategory() != null
                            ? in.getData().getMedicalActCategory().getCode()
                            : null));
            ec.system = triple.system;
            log.info("[AmbulatoryEncounterMapper] [toR4] class.system : {} , valeur", safeToString(ec.system));
            ec.code = triple.code;
            log.info("[AmbulatoryEncounterMapper] [toR4] class.code : {} , valeur", safeToString(ec.code));
            ec.display = triple.display;
            log.info("[AmbulatoryEncounterMapper] [toR4] class.display : {} , valeur", safeToString(ec.display));
            out.encounterClass = ec;
            log.info("[AmbulatoryEncounterMapper] [toR4] out.class : {} , valeur", safeToString(out.encounterClass));

            // type
            if (in.getData() != null && in.getData().getMedicalActCategory() != null) {
                EncounterR4.Coding coding = new EncounterR4.Coding();
                log.info("[AmbulatoryEncounterMapper] [toR4] type.coding(new) : {} , valeur", safeToString(coding));
                coding.code = in.getData().getMedicalActCategory().getCode();
                log.info("[AmbulatoryEncounterMapper] [toR4] type[0].coding[0].code : {} , valeur",
                        safeToString(coding.code));
                coding.display = in.getData().getMedicalActCategory().getLabel();
                log.info("[AmbulatoryEncounterMapper] [toR4] type[0].coding[0].display : {} , valeur",
                        safeToString(coding.display));
                coding.system = MappingConstants.SYS_MEDICAL_ACT_CATEGORY;
                log.info("[AmbulatoryEncounterMapper] [toR4] type[0].coding[0].system : {} , valeur",
                        safeToString(coding.system));
                EncounterR4.CodeableConcept cc = new EncounterR4.CodeableConcept();
                cc.coding = List.of(coding);
                out.type = List.of(cc);
                log.info("[AmbulatoryEncounterMapper] [toR4] out.type.size : {} , valeur", 1);
            }

            // serviceType
            if (in.getData() != null && in.getData().getDenomination() != null) {
                String den = in.getData().getDenomination();
                log.info("[AmbulatoryEncounterMapper] [toR4] denomination(in) : {} , valeur", safeToString(den));
                EncounterR4.Coding coding = new EncounterR4.Coding();
                log.info("[AmbulatoryEncounterMapper] [toR4] serviceType.coding(new) : {} , valeur",
                        safeToString(coding));
                coding.code = normalizeCode(den);
                log.info("[AmbulatoryEncounterMapper] [toR4] serviceType.coding.code : {} , valeur",
                        safeToString(coding.code));
                coding.display = den;
                log.info("[AmbulatoryEncounterMapper] [toR4] serviceType.coding.display : {} , valeur",
                        safeToString(coding.display));
                coding.system = MappingConstants.SYS_SERVICE_DENOMINATION;
                log.info("[AmbulatoryEncounterMapper] [toR4] serviceType.coding.system : {} , valeur",
                        safeToString(coding.system));
                EncounterR4.CodeableConcept cc = new EncounterR4.CodeableConcept();
                cc.coding = List.of(coding);
                out.serviceType = cc;
                log.info("[AmbulatoryEncounterMapper] [toR4] out.serviceType : {} , valeur",
                        safeToString(out.serviceType));
            }

            // subject (patient)
            if (in.getData() != null && in.getData().getPatient() != null) {
                EncounterR4.Reference ref = new EncounterR4.Reference();
                log.info("[AmbulatoryEncounterMapper] [toR4] subject(new) : {} , valeur", safeToString(ref));
                String eyId = in.getData().getPatient().getEyoneInternalId();
                log.info("[AmbulatoryEncounterMapper] [toR4] patient.eyoneInternalId : {} , valeur",
                        safeToString(eyId));
                if (eyId != null) {
                    ref.reference = "Patient/" + eyId;
                    log.info("[AmbulatoryEncounterMapper] [toR4] subject.reference : {} , valeur",
                            safeToString(ref.reference));
                    ref.display = buildPatientDisplay(in.getData().getPatient().getFirstName(),
                            in.getData().getPatient().getLastName());
                    log.info("[AmbulatoryEncounterMapper] [toR4] subject.display : {} , valeur",
                            safeToString(ref.display));
                } else if (in.getData().getPatient().getIdentifier() != null) {
                    ref.reference = "Patient/" + in.getData().getPatient().getIdentifier();
                    log.info("[AmbulatoryEncounterMapper] [toR4] subject.reference : {} , valeur",
                            safeToString(ref.reference));
                }
                out.subject = ref;
                log.info("[AmbulatoryEncounterMapper] [toR4] out.subject : {} , valeur", safeToString(out.subject));
            }

            // period
            if (in.getData() != null && (in.getData().getStartDate() != null || in.getData().getEndDate() != null)) {
                EncounterR4.Period p = new EncounterR4.Period();
                log.info("[AmbulatoryEncounterMapper] [toR4] period(new) : {} , valeur", safeToString(p));
                p.start = DateUtil.toIsoDateTime(in.getData().getStartDate());
                log.info("[AmbulatoryEncounterMapper] [toR4] period.start : {} , valeur", safeToString(p.start));
                p.end = DateUtil.toIsoDateTime(in.getData().getEndDate());
                log.info("[AmbulatoryEncounterMapper] [toR4] period.end : {} , valeur", safeToString(p.end));
                out.period = p;
                log.info("[AmbulatoryEncounterMapper] [toR4] out.period : {} , valeur", safeToString(out.period));
            }

            // serviceProvider (organism)
            if (in.getData() != null && in.getData().getOrganism() != null) {
                EncounterR4.Reference ref = new EncounterR4.Reference();
                log.info("[AmbulatoryEncounterMapper] [toR4] serviceProvider(new) : {} , valeur", safeToString(ref));
                Long id = in.getData().getOrganism().getIdentifier();
                log.info("[AmbulatoryEncounterMapper] [toR4] organism.identifier : {} , valeur", safeToString(id));
                String name = in.getData().getOrganism().getName();
                log.info("[AmbulatoryEncounterMapper] [toR4] organism.name : {} , valeur", safeToString(name));
                if (id != null)
                    ref.reference = "Organization/" + id;
                log.info("[AmbulatoryEncounterMapper] [toR4] serviceProvider.reference : {} , valeur",
                        safeToString(ref.reference));
                ref.display = name;
                log.info("[AmbulatoryEncounterMapper] [toR4] serviceProvider.display : {} , valeur",
                        safeToString(ref.display));
                out.serviceProvider = ref;
                log.info("[AmbulatoryEncounterMapper] [toR4] out.serviceProvider : {} , valeur",
                        safeToString(out.serviceProvider));
            }

            // extensions (R4)
            out.extension = buildEncounterR4Extensions(in);

            // location (from relatedService if present)
            if (in.getData() != null && in.getData().getRelatedService() != null) {
                EncounterR4.Reference locInner = new EncounterR4.Reference();
                Long id = in.getData().getRelatedService().getIdentifier();
                String name = in.getData().getRelatedService().getName();
                if (id != null) {
                    locInner.reference = "Location/" + id;
                    locInner.display = name;
                    EncounterR4.EncounterLocation el = new EncounterR4.EncounterLocation();
                    el.location = locInner;
                    if (out.period != null) {
                        el.period = out.period;
                    }
                    out.location = List.of(el);
                }
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
            if (in == null)
                return null;
            EncounterR5 out = new EncounterR5();
            out.resourceType = MappingConstants.RESOURCE_TYPE_ENCOUNTER;
            log.info("[AmbulatoryEncounterMapper] [toR5] out.resourceType : {} , valeur",
                    safeToString(out.resourceType));
            if (in.getData() != null) {
                out.id = safe(in.getData().getEyoneInternalId());
                log.info("[AmbulatoryEncounterMapper] [toR5] out.id : {} , valeur", safeToString(out.id));
            }

            // meta
            EncounterR5.Meta meta = new EncounterR5.Meta();
            log.info("[AmbulatoryEncounterMapper] [toR5] meta(new) : {} , valeur", safeToString(meta));
            meta.versionId = MappingConstants.META_VERSION;
            log.info("[AmbulatoryEncounterMapper] [toR5] meta.versionId : {} , valeur", safeToString(meta.versionId));
            meta.lastUpdated = DateUtil
                    .toIsoDateTime(safe(in.getData() != null ? in.getData().getCreationDate() : null));
            log.info("[AmbulatoryEncounterMapper] [toR5] meta.lastUpdated : {} , valeur",
                    safeToString(meta.lastUpdated));
            meta.profile = List.of(MappingConstants.PROFILE_ENCOUNTER_AMB);
            log.info("[AmbulatoryEncounterMapper] [toR5] meta.profile[0] : {} , valeur",
                    MappingConstants.PROFILE_ENCOUNTER_AMB);
            out.meta = meta;
            log.info("[AmbulatoryEncounterMapper] [toR5] out.meta : {} , valeur", safeToString(out.meta));

            // identifiers
            out.identifier = new ArrayList<>();
            log.info("[AmbulatoryEncounterMapper] [toR5] out.identifier(new) : {} , valeur",
                    safeToString(out.identifier));
            if (in.getData() != null) {
                addIdentifierR5(out, MappingConstants.SYS_IDENTIFIER_PRESTATION,
                        stringOrNull(in.getData().getIdentifier()));
                addIdentifierR5(out, MappingConstants.SYS_IDENTIFIER_EYONE_INTERNAL, in.getData().getEyoneInternalId());
                addIdentifierR5(out, MappingConstants.SYS_IDENTIFIER_CUSTOM, in.getData().getCustomId());
            }
            addIdentifierR5(out, MappingConstants.SYS_IDENTIFIER_ORGANISM, in.getOrganismId());

            // status
            out.status = mapStatusR4(
                    code(in.getData() != null && in.getData().getStatus() != null ? in.getData().getStatus().getCode()
                            : null));
            log.info("[AmbulatoryEncounterMapper] [toR5] out.status : {} , valeur", safeToString(out.status));

            // class (R5 list)
            EncounterR5.EncounterClass ec = new EncounterR5.EncounterClass();
            log.info("[AmbulatoryEncounterMapper] [toR5] class(new) : {} , valeur", safeToString(ec));
            EncounterClassTriple triple = mapEncounterClass(
                    code(in.getData() != null && in.getData().getMedicalActCategory() != null
                            ? in.getData().getMedicalActCategory().getCode()
                            : null));
            EncounterR5.Coding coding = new EncounterR5.Coding();
            log.info("[AmbulatoryEncounterMapper] [toR5] class.coding(new) : {} , valeur", safeToString(coding));
            coding.system = triple.system;
            log.info("[AmbulatoryEncounterMapper] [toR5] class.coding.system : {} , valeur",
                    safeToString(coding.system));
            coding.code = triple.code;
            log.info("[AmbulatoryEncounterMapper] [toR5] class.coding.code : {} , valeur", safeToString(coding.code));
            coding.display = triple.display;
            log.info("[AmbulatoryEncounterMapper] [toR5] class.coding.display : {} , valeur",
                    safeToString(coding.display));
            ec.coding = List.of(coding);
            out.encounterClass = List.of(ec);
            log.info("[AmbulatoryEncounterMapper] [toR5] out.class.size : {} , valeur", 1);

            // type
            if (in.getData() != null && in.getData().getMedicalActCategory() != null) {
                EncounterR5.Coding typCoding = new EncounterR5.Coding();
                log.info("[AmbulatoryEncounterMapper] [toR5] type.coding(new) : {} , valeur", safeToString(typCoding));
                typCoding.code = in.getData().getMedicalActCategory().getCode();
                log.info("[AmbulatoryEncounterMapper] [toR5] type[0].coding[0].code : {} , valeur",
                        safeToString(typCoding.code));
                typCoding.display = in.getData().getMedicalActCategory().getLabel();
                log.info("[AmbulatoryEncounterMapper] [toR5] type[0].coding[0].display : {} , valeur",
                        safeToString(typCoding.display));
                typCoding.system = MappingConstants.SYS_MEDICAL_ACT_CATEGORY;
                log.info("[AmbulatoryEncounterMapper] [toR5] type[0].coding[0].system : {} , valeur",
                        safeToString(typCoding.system));
                EncounterR5.CodeableConcept cc = new EncounterR5.CodeableConcept();
                cc.coding = List.of(typCoding);
                out.type = List.of(cc);
                log.info("[AmbulatoryEncounterMapper] [toR5] out.type.size : {} , valeur", 1);
            }

            // serviceType (list of ServiceTypeItem with Concept)
            if (in.getData() != null && in.getData().getDenomination() != null) {
                String den = in.getData().getDenomination();
                log.info("[AmbulatoryEncounterMapper] [toR5] denomination(in) : {} , valeur", safeToString(den));
                EncounterR5.Coding svCoding = new EncounterR5.Coding();
                log.info("[AmbulatoryEncounterMapper] [toR5] serviceType.coding(new) : {} , valeur",
                        safeToString(svCoding));
                svCoding.code = normalizeCode(den);
                log.info("[AmbulatoryEncounterMapper] [toR5] serviceType.coding.code : {} , valeur",
                        safeToString(svCoding.code));
                svCoding.display = den;
                log.info("[AmbulatoryEncounterMapper] [toR5] serviceType.coding.display : {} , valeur",
                        safeToString(svCoding.display));
                svCoding.system = MappingConstants.SYS_SERVICE_DENOMINATION;
                log.info("[AmbulatoryEncounterMapper] [toR5] serviceType.coding.system : {} , valeur",
                        safeToString(svCoding.system));
                EncounterR5.Concept concept = new EncounterR5.Concept();
                concept.coding = List.of(svCoding);
                EncounterR5.ServiceTypeItem item = new EncounterR5.ServiceTypeItem();
                item.concept = concept;
                out.serviceType = List.of(item);
                log.info("[AmbulatoryEncounterMapper] [toR5] out.serviceType.size : {} , valeur", 1);
            }

            // subject
            if (in.getData() != null && in.getData().getPatient() != null) {
                EncounterR5.Reference ref = new EncounterR5.Reference();
                log.info("[AmbulatoryEncounterMapper] [toR5] subject(new) : {} , valeur", safeToString(ref));
                String eyId = in.getData().getPatient().getEyoneInternalId();
                log.info("[AmbulatoryEncounterMapper] [toR5] patient.eyoneInternalId : {} , valeur",
                        safeToString(eyId));
                if (eyId != null) {
                    ref.reference = "Patient/" + eyId;
                    log.info("[AmbulatoryEncounterMapper] [toR5] subject.reference : {} , valeur",
                            safeToString(ref.reference));
                    ref.display = buildPatientDisplay(in.getData().getPatient().getFirstName(),
                            in.getData().getPatient().getLastName());
                    log.info("[AmbulatoryEncounterMapper] [toR5] subject.display : {} , valeur",
                            safeToString(ref.display));
                } else if (in.getData().getPatient().getIdentifier() != null) {
                    ref.reference = "Patient/" + in.getData().getPatient().getIdentifier();
                    log.info("[AmbulatoryEncounterMapper] [toR5] subject.reference : {} , valeur",
                            safeToString(ref.reference));
                }
                out.subject = ref;
                log.info("[AmbulatoryEncounterMapper] [toR5] out.subject : {} , valeur", safeToString(out.subject));
            }

            // actualPeriod
            if (in.getData() != null && (in.getData().getStartDate() != null || in.getData().getEndDate() != null)) {
                EncounterR5.Period p = new EncounterR5.Period();
                log.info("[AmbulatoryEncounterMapper] [toR5] actualPeriod(new) : {} , valeur", safeToString(p));
                p.start = DateUtil.toIsoDateTime(in.getData().getStartDate());
                log.info("[AmbulatoryEncounterMapper] [toR5] actualPeriod.start : {} , valeur", safeToString(p.start));
                p.end = DateUtil.toIsoDateTime(in.getData().getEndDate());
                log.info("[AmbulatoryEncounterMapper] [toR5] actualPeriod.end : {} , valeur", safeToString(p.end));
                out.actualPeriod = p;
                log.info("[AmbulatoryEncounterMapper] [toR5] out.actualPeriod : {} , valeur",
                        safeToString(out.actualPeriod));
            }

            // serviceProvider
            if (in.getData() != null && in.getData().getOrganism() != null) {
                EncounterR5.Reference ref = new EncounterR5.Reference();
                log.info("[AmbulatoryEncounterMapper] [toR5] serviceProvider(new) : {} , valeur", safeToString(ref));
                Long id = in.getData().getOrganism().getIdentifier();
                log.info("[AmbulatoryEncounterMapper] [toR5] organism.identifier : {} , valeur", safeToString(id));
                String name = in.getData().getOrganism().getName();
                log.info("[AmbulatoryEncounterMapper] [toR5] organism.name : {} , valeur", safeToString(name));
                if (id != null)
                    ref.reference = "Organization/" + id;
                log.info("[AmbulatoryEncounterMapper] [toR5] serviceProvider.reference : {} , valeur",
                        safeToString(ref.reference));
                ref.display = name;
                log.info("[AmbulatoryEncounterMapper] [toR5] serviceProvider.display : {} , valeur",
                        safeToString(ref.display));
                out.serviceProvider = ref;
                log.info("[AmbulatoryEncounterMapper] [toR5] out.serviceProvider : {} , valeur",
                        safeToString(out.serviceProvider));
            }

            // extensions (R5)
            out.extension = buildEncounterR5Extensions(in);

            // location (from relatedService if present)
            if (in.getData() != null && in.getData().getRelatedService() != null) {
                EncounterR5.Reference locRef = new EncounterR5.Reference();
                Long id = in.getData().getRelatedService().getIdentifier();
                String name = in.getData().getRelatedService().getName();
                if (id != null) {
                    locRef.reference = "Location/" + id;
                    locRef.display = name;
                    EncounterR5.EncounterLocation el = new EncounterR5.EncounterLocation();
                    el.location = locRef;
                    if (out.actualPeriod != null) {
                        el.period = out.actualPeriod;
                    }
                    out.location = List.of(el);
                }
            }

            log.info("[AmbulatoryEncounterMapper] [toR5] output : {} , valeur", safeToString(out));
            return out;
        } catch (Exception e) {
            log.info("[AmbulatoryEncounterMapper] [toR5] exception : {} , valeur", e.toString(), e);
            throw new RuntimeException("AmbulatoryEncounterMapper.toR5 failed", e);
        }
    }

    private static List<EncounterR5.Extension> buildEncounterR5Extensions(Ambulatory in) {
        List<EncounterR5.Extension> exts = new ArrayList<>();
        if (in == null)
            return exts;

        // messageType
        addExt(exts, simpleExt(MappingConstants.SD_MESSAGE_TYPE, in.getMessageType()));
        // messageKey (compose si possible)
        String eyId = in.getData() != null ? in.getData().getEyoneInternalId() : null;
        String orgId = in.getOrganismId() != null ? String.valueOf(in.getOrganismId()) : null;
        String msgKey = (in.getAction() != null && eyId != null && orgId != null)
                ? (in.getAction() + "_" + eyId + "_" + orgId)
                : null;
        addExt(exts, simpleExt(MappingConstants.SD_MESSAGE_KEY, msgKey));
        // action
        addExt(exts, simpleExt(MappingConstants.SD_ACTION, in.getAction()));
        // origin
        addExt(exts, simpleExt(MappingConstants.SD_ORIGIN, in.getOrigin()));
        // messageStatus (fallback PENDING si on-going)
        String messageStatus = "PENDING";
        if (in.getData() != null && in.getData().getStatus() != null && "VALIDATED".equalsIgnoreCase(
                safeStr(in.getData().getStatus().getCode()))) {
            messageStatus = "DONE";
        }
        addExt(exts, simpleExt(MappingConstants.SD_MESSAGE_STATUS, messageStatus));
        // createdBy
        addExt(exts, simpleExt(MappingConstants.SD_CREATED_BY,
                in.getData() != null ? in.getData().getCreatedBy() : null));
        // creationDate
        addExt(exts, childDateTime(MappingConstants.SD_CREATION_DATE,
                DateUtil.toIsoDateTime(in.getData() != null ? in.getData().getCreationDate() : null)));

        // medicalActCategory (nested)
        if (in.getData() != null && in.getData().getMedicalActCategory() != null) {
            List<EncounterR5.Extension> macChildren = new ArrayList<>();
            addChild(macChildren, child("code", in.getData().getMedicalActCategory().getCode()));
            addChild(macChildren, child("label", in.getData().getMedicalActCategory().getLabel()));
            addChild(macChildren, child("evacuation", in.getData().getMedicalActCategory().getEvacuation()));
            addChild(macChildren, child("modeleItem", in.getData().getMedicalActCategory().getModeleItem()));
            addChild(macChildren, child("medicalExam", in.getData().getMedicalActCategory().getMedicalExam()));
            addChild(macChildren, child("pharmacyAsPrestation", in.getData().getMedicalActCategory().getPharmacyAsPrestation()));
            addChild(macChildren, child("hospitalization", in.getData().getMedicalActCategory().getHospitalization()));
            addChild(macChildren, child("analysis", in.getData().getMedicalActCategory().getAnalysis()));
            addChild(macChildren, child("radiology", in.getData().getMedicalActCategory().getRadiology()));
            addChild(macChildren, child("quote", in.getData().getMedicalActCategory().getQuote()));
            addChild(macChildren, child("all", in.getData().getMedicalActCategory().getAll()));
            addChild(macChildren, child("editable", in.getData().getMedicalActCategory().getEditable()));
            addChild(macChildren, child("room", in.getData().getMedicalActCategory().getRoom()));
            addChild(macChildren, child("medoc", in.getData().getMedicalActCategory().getMedoc()));
            addChild(macChildren, child("modele", in.getData().getMedicalActCategory().getModele()));
            addChild(macChildren, child("visit", in.getData().getMedicalActCategory().getVisit()));
            addChild(macChildren, child("ambulatory", in.getData().getMedicalActCategory().getAmbulatory()));
            addChild(macChildren, child("consultation", in.getData().getMedicalActCategory().getConsultation()));
            if (!macChildren.isEmpty()) {
                EncounterR5.Extension mac = new EncounterR5.Extension();
                mac.url = MappingConstants.SD_MEDICAL_ACT_CATEGORY;
                mac.extension = macChildren;
                exts.add(mac);
            }
        }

        // statusDetails (nested)
        if (in.getData() != null && in.getData().getStatus() != null) {
            List<EncounterR5.Extension> sdChildren = new ArrayList<>();
            addChild(sdChildren, child("code", in.getData().getStatus().getCode()));
            addChild(sdChildren, child("label", in.getData().getStatus().getLabel()));
            addChild(sdChildren, child("done", in.getData().getStatus().getDone()));
            addChild(sdChildren, child("created", in.getData().getStatus().getCreated()));
            addChild(sdChildren, child("cancelled", in.getData().getStatus().getCancelled()));
            addChild(sdChildren, child("toBeConfirmed", in.getData().getStatus().getToBeConfirmed()));
            addChild(sdChildren, child("notAccepted", in.getData().getStatus().getNotAccepted()));
            addChild(sdChildren, child("onGoing", in.getData().getStatus().getOnGoing()));
            addChild(sdChildren, child("unavailable", in.getData().getStatus().getUnavailable()));
            addChild(sdChildren, child("notPerformed", in.getData().getStatus().getNotPerformed()));
            if (!sdChildren.isEmpty()) {
                EncounterR5.Extension sd = new EncounterR5.Extension();
                sd.url = MappingConstants.SD_STATUS_DETAILS;
                sd.extension = sdChildren;
                exts.add(sd);
            }
        }

        // pharmacySellPrestation (nested)
        if (in.getData() != null && in.getData().getPharmacySellPrestation() != null) {
            List<EncounterR5.Extension> pspChildren = new ArrayList<>();
            addChild(pspChildren, child("identifier", in.getData().getPharmacySellPrestation().getIdentifier()));
            addChild(pspChildren, child("disable", in.getData().getPharmacySellPrestation().getDisable()));
            addChild(pspChildren, child("nbrProducts", in.getData().getPharmacySellPrestation().getNbrProducts()));
            addChild(pspChildren, child("commentary", in.getData().getPharmacySellPrestation().getCommentary()));
            if (in.getData().getPharmacySellPrestation().getPharmacyBillingMode() != null) {
                List<EncounterR5.Extension> pbmChildren = new ArrayList<>();
                addChild(pbmChildren, child("code", in.getData().getPharmacySellPrestation().getPharmacyBillingMode().getCode()));
                addChild(pbmChildren, child("label", in.getData().getPharmacySellPrestation().getPharmacyBillingMode().getLabel()));
                addChild(pbmChildren, child("scope", in.getData().getPharmacySellPrestation().getPharmacyBillingMode().getScope()));
                addChild(pbmChildren, child("global", in.getData().getPharmacySellPrestation().getPharmacyBillingMode().getGlobal()));
                addChild(pbmChildren, child("detail", in.getData().getPharmacySellPrestation().getPharmacyBillingMode().getDetail()));
                if (!pbmChildren.isEmpty()) {
                    EncounterR5.Extension pbm = new EncounterR5.Extension();
                    pbm.url = "pharmacyBillingMode";
                    pbm.extension = pbmChildren;
                    pspChildren.add(pbm);
                }
            }
            addChild(pspChildren, child("usedAsAPrestation", in.getData().getPharmacySellPrestation().getUsedAsAPrestation()));
            addChild(pspChildren, child("manageStock", in.getData().getPharmacySellPrestation().getManageStock()));
            addChild(pspChildren, child("documentGenerationRequired2", in.getData().getPharmacySellPrestation().getDocumentGenerationRequired2()));
            if (!pspChildren.isEmpty()) {
                EncounterR5.Extension psp = new EncounterR5.Extension();
                psp.url = MappingConstants.SD_PHARMACY_SELL_PRESTATION;
                psp.extension = pspChildren;
                exts.add(psp);
            }
        }

        // toothPrestation (fallback false if absent) — envoyé explicitement à false
        addExt(exts, child(MappingConstants.SD_TOOTH_PRESTATION, false));

        // editor (nested)
        if (in.getData() != null && in.getData().getEditor() != null) {
            List<EncounterR5.Extension> editorChildren = new ArrayList<>();
            if (in.getData() != null && in.getData().getEditor().getOrganism() != null) {
                List<EncounterR5.Extension> orgChildren = new ArrayList<>();
                addChild(orgChildren, child("identifier", in.getData().getEditor().getOrganism().getIdentifier()));
                addChild(orgChildren, child("name", in.getData().getEditor().getOrganism().getName()));
                if (!orgChildren.isEmpty()) {
                    EncounterR5.Extension org = new EncounterR5.Extension();
                    org.url = "organism";
                    org.extension = orgChildren;
                    editorChildren.add(org);
                }
            }
            addChild(editorChildren, child("userId", in.getData().getEditor().getUserId()));
            addChild(editorChildren, child("userName", in.getData().getEditor().getUserName()));
            addChild(editorChildren, child("completeName", in.getData().getEditor().getCompleteName()));
            addChild(editorChildren, child("identifier", in.getData().getEditor().getIdentifier()));
            addChild(editorChildren, child("completeNameAndUserName", in.getData().getEditor().getCompleteNameAndUserName()));
            if (in.getData().getEditor().getUserDetailsInfos() != null) {
                List<EncounterR5.Extension> udiChildren = new ArrayList<>();
                addChild(udiChildren, child("hasSuperviseurRight", in.getData().getEditor().getUserDetailsInfos().getHasSuperviseurRight()));
                addChild(udiChildren, child("doctor", in.getData().getEditor().getUserDetailsInfos().getDoctor()));
                if (!udiChildren.isEmpty()) {
                    EncounterR5.Extension udi = new EncounterR5.Extension();
                    udi.url = "userDetailsInfos";
                    udi.extension = udiChildren;
                    editorChildren.add(udi);
                }
            }
            if (!editorChildren.isEmpty()) {
                EncounterR5.Extension ed = new EncounterR5.Extension();
                ed.url = MappingConstants.SD_EDITOR;
                ed.extension = editorChildren;
                exts.add(ed);
            }
        }

        // relatedService
        if (in.getData() != null && in.getData().getRelatedService() != null) {
            List<EncounterR5.Extension> rsChildren = new ArrayList<>();
            addChild(rsChildren, child("identifier", in.getData().getRelatedService().getIdentifier()));
            addChild(rsChildren, child("name", in.getData().getRelatedService().getName()));
            if (!rsChildren.isEmpty()) {
                EncounterR5.Extension rs = new EncounterR5.Extension();
                rs.url = MappingConstants.SD_RELATED_SERVICE;
                rs.extension = rsChildren;
                exts.add(rs);
            }
        }

        // claims from root
        if (in.getClaims() != null) {
            List<EncounterR5.Extension> claimsChildren = new ArrayList<>();
            addChild(claimsChildren, child("endpoint", MappingConstants.CLAIMS_ENDPOINT_MEDICAL_WORKFLOW_STATUS));
            addChild(claimsChildren, child("method", in.getClaims().getMethod()));
            addChild(claimsChildren, child("class", in.getClaims().getClassName()));
            if (!claimsChildren.isEmpty()) {
                EncounterR5.Extension claims = new EncounterR5.Extension();
                claims.url = MappingConstants.SD_CLAIMS;
                claims.extension = claimsChildren;
                exts.add(claims);
            }
        }

        return exts;
    }

    private static List<EncounterR4.Extension> buildEncounterR4Extensions(Ambulatory in) {
        List<EncounterR4.Extension> exts = new ArrayList<>();
        if (in == null) return exts;

        // simple string/date extensions
        addExt(exts, simpleExtR4(MappingConstants.SD_MESSAGE_TYPE, in.getMessageType()));
        String eyId = in.getData() != null ? in.getData().getEyoneInternalId() : null;
        String orgId = in.getOrganismId() != null ? String.valueOf(in.getOrganismId()) : null;
        String msgKey = (in.getAction() != null && eyId != null && orgId != null)
                ? (in.getAction() + "_" + eyId + "_" + orgId) : null;
        addExt(exts, simpleExtR4(MappingConstants.SD_MESSAGE_KEY, msgKey));
        addExt(exts, simpleExtR4(MappingConstants.SD_ACTION, in.getAction()));
        addExt(exts, simpleExtR4(MappingConstants.SD_ORIGIN, in.getOrigin()));
        String messageStatus = "PENDING";
        if (in.getData() != null && in.getData().getStatus() != null && "VALIDATED".equalsIgnoreCase(
                safeStr(in.getData().getStatus().getCode()))) {
            messageStatus = "DONE";
        }
        addExt(exts, simpleExtR4(MappingConstants.SD_MESSAGE_STATUS, messageStatus));
        addExt(exts, simpleExtR4(MappingConstants.SD_CREATED_BY, in.getData() != null ? in.getData().getCreatedBy() : null));
        addExt(exts, childDateTimeR4(MappingConstants.SD_CREATION_DATE,
                DateUtil.toIsoDateTime(in.getData() != null ? in.getData().getCreationDate() : null)));

        // medicalActCategory (nested)
        if (in.getData() != null && in.getData().getMedicalActCategory() != null) {
            List<EncounterR4.Extension> macChildren = new ArrayList<>();
            addChild(macChildren, childR4("code", in.getData().getMedicalActCategory().getCode()));
            addChild(macChildren, childR4("label", in.getData().getMedicalActCategory().getLabel()));
            addChild(macChildren, childR4("evacuation", in.getData().getMedicalActCategory().getEvacuation()));
            addChild(macChildren, childR4("modeleItem", in.getData().getMedicalActCategory().getModeleItem()));
            addChild(macChildren, childR4("medicalExam", in.getData().getMedicalActCategory().getMedicalExam()));
            addChild(macChildren, childR4("pharmacyAsPrestation", in.getData().getMedicalActCategory().getPharmacyAsPrestation()));
            addChild(macChildren, childR4("hospitalization", in.getData().getMedicalActCategory().getHospitalization()));
            addChild(macChildren, childR4("analysis", in.getData().getMedicalActCategory().getAnalysis()));
            addChild(macChildren, childR4("radiology", in.getData().getMedicalActCategory().getRadiology()));
            addChild(macChildren, childR4("quote", in.getData().getMedicalActCategory().getQuote()));
            addChild(macChildren, childR4("all", in.getData().getMedicalActCategory().getAll()));
            addChild(macChildren, childR4("editable", in.getData().getMedicalActCategory().getEditable()));
            addChild(macChildren, childR4("room", in.getData().getMedicalActCategory().getRoom()));
            addChild(macChildren, childR4("medoc", in.getData().getMedicalActCategory().getMedoc()));
            addChild(macChildren, childR4("modele", in.getData().getMedicalActCategory().getModele()));
            addChild(macChildren, childR4("visit", in.getData().getMedicalActCategory().getVisit()));
            addChild(macChildren, childR4("ambulatory", in.getData().getMedicalActCategory().getAmbulatory()));
            addChild(macChildren, childR4("consultation", in.getData().getMedicalActCategory().getConsultation()));
            if (!macChildren.isEmpty()) {
                EncounterR4.Extension mac = new EncounterR4.Extension();
                mac.url = MappingConstants.SD_MEDICAL_ACT_CATEGORY;
                mac.extension = macChildren;
                exts.add(mac);
            }
        }

        // statusDetails (nested)
        if (in.getData() != null && in.getData().getStatus() != null) {
            List<EncounterR4.Extension> sdChildren = new ArrayList<>();
            addChild(sdChildren, childR4("code", in.getData().getStatus().getCode()));
            addChild(sdChildren, childR4("label", in.getData().getStatus().getLabel()));
            addChild(sdChildren, childR4("done", in.getData().getStatus().getDone()));
            addChild(sdChildren, childR4("created", in.getData().getStatus().getCreated()));
            addChild(sdChildren, childR4("cancelled", in.getData().getStatus().getCancelled()));
            addChild(sdChildren, childR4("toBeConfirmed", in.getData().getStatus().getToBeConfirmed()));
            addChild(sdChildren, childR4("notAccepted", in.getData().getStatus().getNotAccepted()));
            addChild(sdChildren, childR4("onGoing", in.getData().getStatus().getOnGoing()));
            addChild(sdChildren, childR4("unavailable", in.getData().getStatus().getUnavailable()));
            addChild(sdChildren, childR4("notPerformed", in.getData().getStatus().getNotPerformed()));
            if (!sdChildren.isEmpty()) {
                EncounterR4.Extension sd = new EncounterR4.Extension();
                sd.url = MappingConstants.SD_STATUS_DETAILS;
                sd.extension = sdChildren;
                exts.add(sd);
            }
        }

        // editor (nested, limited per SI)
        if (in.getData() != null && in.getData().getEditor() != null) {
            List<EncounterR4.Extension> editorChildren = new ArrayList<>();
            if (in.getData().getEditor().getOrganism() != null) {
                List<EncounterR4.Extension> orgChildren = new ArrayList<>();
                addChild(orgChildren, childR4("identifier", in.getData().getEditor().getOrganism().getIdentifier()));
                addChild(orgChildren, childR4("name", in.getData().getEditor().getOrganism().getName()));
                if (!orgChildren.isEmpty()) {
                    EncounterR4.Extension org = new EncounterR4.Extension();
                    org.url = "organism";
                    org.extension = orgChildren;
                    editorChildren.add(org);
                }
            }
            addChild(editorChildren, childR4("userId", in.getData().getEditor().getUserId()));
            addChild(editorChildren, childR4("userName", in.getData().getEditor().getUserName()));
            addChild(editorChildren, childR4("completeName", in.getData().getEditor().getCompleteName()));
            addChild(editorChildren, childR4("identifier", in.getData().getEditor().getIdentifier()));
            addChild(editorChildren, childR4("completeNameAndUserName", in.getData().getEditor().getCompleteNameAndUserName()));
            if (in.getData().getEditor().getUserDetailsInfos() != null) {
                List<EncounterR4.Extension> udiChildren = new ArrayList<>();
                addChild(udiChildren, childR4("hasSuperviseurRight", in.getData().getEditor().getUserDetailsInfos().getHasSuperviseurRight()));
                addChild(udiChildren, childR4("doctor", in.getData().getEditor().getUserDetailsInfos().getDoctor()));
                if (!udiChildren.isEmpty()) {
                    EncounterR4.Extension udi = new EncounterR4.Extension();
                    udi.url = "userDetailsInfos";
                    udi.extension = udiChildren;
                    editorChildren.add(udi);
                }
            }
            if (!editorChildren.isEmpty()) {
                EncounterR4.Extension ed = new EncounterR4.Extension();
                ed.url = MappingConstants.SD_EDITOR;
                ed.extension = editorChildren;
                exts.add(ed);
            }
        }

        // relatedService
        if (in.getData() != null && in.getData().getRelatedService() != null) {
            List<EncounterR4.Extension> rsChildren = new ArrayList<>();
            addChild(rsChildren, childR4("identifier", in.getData().getRelatedService().getIdentifier()));
            addChild(rsChildren, childR4("name", in.getData().getRelatedService().getName()));
            if (!rsChildren.isEmpty()) {
                EncounterR4.Extension rs = new EncounterR4.Extension();
                rs.url = MappingConstants.SD_RELATED_SERVICE;
                rs.extension = rsChildren;
                exts.add(rs);
            }
        }

        // claims from root
        if (in.getClaims() != null) {
            List<EncounterR4.Extension> claimsChildren = new ArrayList<>();
            addChild(claimsChildren, childR4("endpoint", MappingConstants.CLAIMS_ENDPOINT_MEDICAL_WORKFLOW_STATUS));
            addChild(claimsChildren, childR4("method", in.getClaims().getMethod()));
            addChild(claimsChildren, childR4("class", in.getClaims().getClassName()));
            if (!claimsChildren.isEmpty()) {
                EncounterR4.Extension claims = new EncounterR4.Extension();
                claims.url = MappingConstants.SD_CLAIMS;
                claims.extension = claimsChildren;
                exts.add(claims);
            }
        }

        // Optional R4-only flags if you have source (not present in SI sample):
        // firstMedicalSellPrestation, firstConsultation, telePrestation, createByQueue, documentIssuerStatus
        // Skipped unless inputs exist.

        return exts;
    }

    private static void addExt(List<EncounterR5.Extension> list, EncounterR5.Extension e) {
        if (e != null) list.add(e);
    }

    private static void addChild(List<EncounterR5.Extension> list, EncounterR5.Extension e) {
        if (e != null) list.add(e);
    }

    private static EncounterR5.Extension simpleExt(String url, String value) {
        if (value == null || (value instanceof String && ((String) value).isBlank())) return null;
        EncounterR5.Extension e = new EncounterR5.Extension();
        e.url = url;
        e.valueString = value;
        return e;
    }

    private static EncounterR5.Extension child(String url, String value) {
        if (value == null || value.isBlank()) return null;
        EncounterR5.Extension e = new EncounterR5.Extension();
        e.url = url;
        e.valueString = value;
        return e;
    }

    private static EncounterR5.Extension child(String url, Boolean value) {
        if (value == null) return null;
        EncounterR5.Extension e = new EncounterR5.Extension();
        e.url = url;
        e.valueBoolean = value;
        return e;
    }

    private static EncounterR5.Extension child(String url, Integer value) {
        if (value == null) return null;
        EncounterR5.Extension e = new EncounterR5.Extension();
        e.url = url;
        e.valueInteger = value;
        return e;
    }

    private static EncounterR5.Extension child(String url, Long value) {
        if (value == null) return null;
        EncounterR5.Extension e = new EncounterR5.Extension();
        e.url = url;
        e.valueInteger = Math.toIntExact(value);
        return e;
    }

    private static EncounterR5.Extension childDateTime(String url, String dateTimeIso) {
        if (dateTimeIso == null || dateTimeIso.isBlank()) return null;
        EncounterR5.Extension e = new EncounterR5.Extension();
        e.url = url;
        e.valueDateTime = dateTimeIso;
        return e;
    }

    private static void addIdentifierR4(EncounterR4 out, String system, String value) {
        if (value == null || value.isBlank())
            return;
        EncounterR4.Identifier id = new EncounterR4.Identifier();
        log.info("[AmbulatoryEncounterMapper] [addIdentifierR4] identifier(new) : {} , valeur", safeToString(id));
        id.system = system;
        log.info("[AmbulatoryEncounterMapper] [addIdentifierR4] identifier.system : {} , valeur",
                safeToString(id.system));
        id.value = value;
        log.info("[AmbulatoryEncounterMapper] [addIdentifierR4] identifier.value : {} , valeur",
                safeToString(id.value));
        out.identifier.add(id);
        log.info("[AmbulatoryEncounterMapper] [addIdentifierR4] out.identifier.size : {} , valeur",
                out.identifier.size());
    }

    private static void addIdentifierR5(EncounterR5 out, String system, String value) {
        if (value == null || value.isBlank())
            return;
        EncounterR5.Identifier id = new EncounterR5.Identifier();
        log.info("[AmbulatoryEncounterMapper] [addIdentifierR5] identifier(new) : {} , valeur", safeToString(id));
        id.system = system;
        log.info("[AmbulatoryEncounterMapper] [addIdentifierR5] identifier.system : {} , valeur",
                safeToString(id.system));
        id.value = value;
        log.info("[AmbulatoryEncounterMapper] [addIdentifierR5] identifier.value : {} , valeur",
                safeToString(id.value));
        out.identifier.add(id);
        log.info("[AmbulatoryEncounterMapper] [addIdentifierR5] out.identifier.size : {} , valeur",
                out.identifier.size());
    }

    private static String mapStatusR4(String code) {
        if (code == null)
            return null;
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
        String system = MappingConstants.SYS_V3_ACTCODE;
        String code = "AMB";
        String display = "ambulatory";
        if (siCode != null) {
            switch (siCode) {
                case "AMBULATORY":
                case "CONSULTATION":
                    code = "AMB";
                    display = "ambulatory";
                    break;
                case "HOSPITALIZATION":
                    code = "IMP";
                    display = "inpatient encounter";
                    break;
                case "VISIT":
                    code = "HH";
                    display = "home health";
                    break;
                case "EMERGENCY":
                    code = "EMER";
                    display = "emergency";
                    break;
                default:
                    // keep defaults
                    break;
            }
        }
        return new EncounterClassTriple(system, code, display);
    }

    private static class EncounterClassTriple {
        public final String system;
        public final String code;
        public final String display;

        private EncounterClassTriple(String system, String code, String display) {
            this.system = system;
            this.code = code;
            this.display = display;
        }
    }

    private static String safe(String s) {
        return s == null ? null : s;
    }

    private static String stringOrNull(Object o) {
        return o == null ? null : Objects.toString(o);
    }

    private static String code(String s) {
        return s == null ? null : s.trim();
    }

    private static String buildPatientDisplay(String first, String last) {
        String f = first == null ? "" : first;
        String l = last == null ? "" : last;
        String v = (f + " " + l).trim();
        return v.isEmpty() ? null : v;
    }

    private static String normalizeCode(String s) {
        if (s == null)
            return null;
        return s.trim().toUpperCase().replace(' ', '_');
    }

    private static String safeStr(String s) {
        return s == null ? null : s;
    }

    private static String safeToString(Object value) {
        if (value == null)
            return "null";
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return value.getClass().getSimpleName();
        }
    }
}
