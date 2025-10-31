package net.eyone.yonewi_mapping.mapper;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.eyone.yonewi_mapping.constant.MappingConstants;
import net.eyone.yonewi_mapping.model.input.Ambulatory;
import net.eyone.yonewi_mapping.model.output.EncounterR4;
import net.eyone.yonewi_mapping.model.output.EncounterR5;
import net.eyone.yonewi_mapping.service.DateUtil;

@Slf4j
public final class EncounterExtensionsMapper {

    private EncounterExtensionsMapper() {}

    public static List<EncounterR5.Extension> buildEncounterR5Extensions(Ambulatory in) {
        log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] start : {} , valeur", safeToString(in));
        List<EncounterR5.Extension> exts = new ArrayList<>();
        if (in == null) return exts;

        addExt(exts, simpleExt(MappingConstants.SD_MESSAGE_TYPE, in.getMessageType())); log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] messageType : {} , valeur", safeToString(in.getMessageType()));
        String eyId = in.getData() != null ? in.getData().getEyoneInternalId() : null;
        String orgId = in.getOrganismId() != null ? String.valueOf(in.getOrganismId()) : null;
        String msgKey = (in.getAction() != null && eyId != null && orgId != null) ? (in.getAction() + "_" + eyId + "_" + orgId) : null;
        addExt(exts, simpleExt(MappingConstants.SD_MESSAGE_KEY, msgKey)); log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] messageKey : {} , valeur", safeToString(msgKey));
        addExt(exts, simpleExt(MappingConstants.SD_ACTION, in.getAction())); log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] action : {} , valeur", safeToString(in.getAction()));
        addExt(exts, simpleExt(MappingConstants.SD_ORIGIN, in.getOrigin())); log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] origin : {} , valeur", safeToString(in.getOrigin()));
        String messageStatus = "PENDING";
        if (in.getData() != null && in.getData().getStatus() != null && "VALIDATED".equalsIgnoreCase(safeStr(in.getData().getStatus().getCode()))) {
            messageStatus = "DONE";
        }
        addExt(exts, simpleExt(MappingConstants.SD_MESSAGE_STATUS, messageStatus)); log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] messageStatus : {} , valeur", safeToString(messageStatus));
        addExt(exts, simpleExt(MappingConstants.SD_CREATED_BY, in.getData() != null ? in.getData().getCreatedBy() : null)); log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] createdBy : {} , valeur", safeToString(in.getData() != null ? in.getData().getCreatedBy() : null));
        String creationDateIso = DateUtil.toIsoDateTime(in.getData() != null ? in.getData().getCreationDate() : null);
        addExt(exts, childDateTime(MappingConstants.SD_CREATION_DATE, creationDateIso)); log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] creationDate : {} , valeur", safeToString(creationDateIso));

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
                EncounterR5.Extension mac = new EncounterR5.Extension(); mac.url = MappingConstants.SD_MEDICAL_ACT_CATEGORY; mac.extension = macChildren; exts.add(mac);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] medicalActCategory.children : {} , valeur", macChildren.size());
            }
        }

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
                EncounterR5.Extension sd = new EncounterR5.Extension(); sd.url = MappingConstants.SD_STATUS_DETAILS; sd.extension = sdChildren; exts.add(sd);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] statusDetails.children : {} , valeur", sdChildren.size());
            }
        }

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
                    EncounterR5.Extension pbm = new EncounterR5.Extension(); pbm.url = "pharmacyBillingMode"; pbm.extension = pbmChildren; pspChildren.add(pbm);
                    log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] pharmacyBillingMode.children : {} , valeur", pbmChildren.size());
                }
            }
            addChild(pspChildren, child("usedAsAPrestation", in.getData().getPharmacySellPrestation().getUsedAsAPrestation()));
            addChild(pspChildren, child("manageStock", in.getData().getPharmacySellPrestation().getManageStock()));
            addChild(pspChildren, child("documentGenerationRequired2", in.getData().getPharmacySellPrestation().getDocumentGenerationRequired2()));
            if (!pspChildren.isEmpty()) {
                EncounterR5.Extension psp = new EncounterR5.Extension(); psp.url = MappingConstants.SD_PHARMACY_SELL_PRESTATION; psp.extension = pspChildren; exts.add(psp);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] pharmacySellPrestation.children : {} , valeur", pspChildren.size());
            }
        }

        addExt(exts, child(MappingConstants.SD_TOOTH_PRESTATION, false)); log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] toothPrestation : {} , valeur", false);

        if (in.getData() != null && in.getData().getEditor() != null) {
            List<EncounterR5.Extension> editorChildren = new ArrayList<>();
            if (in.getData() != null && in.getData().getEditor().getOrganism() != null) {
                List<EncounterR5.Extension> orgChildren = new ArrayList<>();
                addChild(orgChildren, child("identifier", in.getData().getEditor().getOrganism().getIdentifier()));
                addChild(orgChildren, child("name", in.getData().getEditor().getOrganism().getName()));
                if (!orgChildren.isEmpty()) {
                    EncounterR5.Extension org = new EncounterR5.Extension(); org.url = "organism"; org.extension = orgChildren; editorChildren.add(org);
                    log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] editor.organism.children : {} , valeur", orgChildren.size());
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
                    EncounterR5.Extension udi = new EncounterR5.Extension(); udi.url = "userDetailsInfos"; udi.extension = udiChildren; editorChildren.add(udi);
                    log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] editor.userDetailsInfos.children : {} , valeur", udiChildren.size());
                }
            }
            if (!editorChildren.isEmpty()) {
                EncounterR5.Extension ed = new EncounterR5.Extension(); ed.url = MappingConstants.SD_EDITOR; ed.extension = editorChildren; exts.add(ed);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] editor.children : {} , valeur", editorChildren.size());
            }
        }

        if (in.getData() != null && in.getData().getRelatedService() != null) {
            List<EncounterR5.Extension> rsChildren = new ArrayList<>();
            addChild(rsChildren, child("identifier", in.getData().getRelatedService().getIdentifier()));
            addChild(rsChildren, child("name", in.getData().getRelatedService().getName()));
            if (!rsChildren.isEmpty()) {
                EncounterR5.Extension rs = new EncounterR5.Extension(); rs.url = MappingConstants.SD_RELATED_SERVICE; rs.extension = rsChildren; exts.add(rs);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] relatedService.children : {} , valeur", rsChildren.size());
            }
        }

        if (in.getClaims() != null) {
            List<EncounterR5.Extension> claimsChildren = new ArrayList<>();
            addChild(claimsChildren, child("endpoint", MappingConstants.CLAIMS_ENDPOINT_MEDICAL_WORKFLOW_STATUS));
            addChild(claimsChildren, child("method", in.getClaims().getMethod()));
            addChild(claimsChildren, child("class", in.getClaims().getClassName()));
            if (!claimsChildren.isEmpty()) {
                EncounterR5.Extension claims = new EncounterR5.Extension(); claims.url = MappingConstants.SD_CLAIMS; claims.extension = claimsChildren; exts.add(claims);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] claims.children : {} , valeur", claimsChildren.size());
            }
        }

        log.info("[AmbulatoryEncounterMapper] [buildEncounterR5Extensions] end.size : {} , valeur", exts.size());
        return exts;
    }

    public static List<EncounterR4.Extension> buildEncounterR4Extensions(Ambulatory in) {
        log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] start : {} , valeur", safeToString(in));
        List<EncounterR4.Extension> exts = new ArrayList<>();
        if (in == null) return exts;

        addExt(exts, simpleExtR4(MappingConstants.SD_MESSAGE_TYPE, in.getMessageType())); log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] messageType : {} , valeur", safeToString(in.getMessageType()));
        String eyId = in.getData() != null ? in.getData().getEyoneInternalId() : null;
        String orgId = in.getOrganismId() != null ? String.valueOf(in.getOrganismId()) : null;
        String msgKey = (in.getAction() != null && eyId != null && orgId != null) ? (in.getAction() + "_" + eyId + "_" + orgId) : null;
        addExt(exts, simpleExtR4(MappingConstants.SD_MESSAGE_KEY, msgKey)); log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] messageKey : {} , valeur", safeToString(msgKey));
        addExt(exts, simpleExtR4(MappingConstants.SD_ACTION, in.getAction())); log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] action : {} , valeur", safeToString(in.getAction()));
        addExt(exts, simpleExtR4(MappingConstants.SD_ORIGIN, in.getOrigin())); log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] origin : {} , valeur", safeToString(in.getOrigin()));
        String messageStatus = "PENDING";
        if (in.getData() != null && in.getData().getStatus() != null && "VALIDATED".equalsIgnoreCase(safeStr(in.getData().getStatus().getCode()))) {
            messageStatus = "DONE";
        }
        addExt(exts, simpleExtR4(MappingConstants.SD_MESSAGE_STATUS, messageStatus)); log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] messageStatus : {} , valeur", safeToString(messageStatus));
        addExt(exts, simpleExtR4(MappingConstants.SD_CREATED_BY, in.getData() != null ? in.getData().getCreatedBy() : null)); log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] createdBy : {} , valeur", safeToString(in.getData() != null ? in.getData().getCreatedBy() : null));
        String creationDateIso = DateUtil.toIsoDateTime(in.getData() != null ? in.getData().getCreationDate() : null);
        addExt(exts, childDateTimeR4(MappingConstants.SD_CREATION_DATE, creationDateIso)); log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] creationDate : {} , valeur", safeToString(creationDateIso));

        if (in.getData() == null) return exts;
        if (in.getData().getMedicalActCategory() != null) {
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
                EncounterR4.Extension mac = new EncounterR4.Extension(); mac.url = MappingConstants.SD_MEDICAL_ACT_CATEGORY; mac.extension = macChildren; exts.add(mac);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] medicalActCategory.children : {} , valeur", macChildren.size());
            }
        }

        if (in.getData().getStatus() != null) {
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
                EncounterR4.Extension sd = new EncounterR4.Extension(); sd.url = MappingConstants.SD_STATUS_DETAILS; sd.extension = sdChildren; exts.add(sd);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] statusDetails.children : {} , valeur", sdChildren.size());
            }
        }

        if (in.getData().getEditor() != null) {
            List<EncounterR4.Extension> editorChildren = new ArrayList<>();
            if (in.getData().getEditor().getOrganism() != null) {
                List<EncounterR4.Extension> orgChildren = new ArrayList<>();
                addChild(orgChildren, childR4("identifier", in.getData().getEditor().getOrganism().getIdentifier()));
                addChild(orgChildren, childR4("name", in.getData().getEditor().getOrganism().getName()));
                if (!orgChildren.isEmpty()) {
                    EncounterR4.Extension org = new EncounterR4.Extension(); org.url = "organism"; org.extension = orgChildren; editorChildren.add(org);
                    log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] editor.organism.children : {} , valeur", orgChildren.size());
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
                    EncounterR4.Extension udi = new EncounterR4.Extension(); udi.url = "userDetailsInfos"; udi.extension = udiChildren; editorChildren.add(udi);
                    log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] editor.userDetailsInfos.children : {} , valeur", udiChildren.size());
                }
            }
            if (!editorChildren.isEmpty()) {
                EncounterR4.Extension ed = new EncounterR4.Extension(); ed.url = MappingConstants.SD_EDITOR; ed.extension = editorChildren; exts.add(ed);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] editor.children : {} , valeur", editorChildren.size());
            }
        }

        if (in.getData().getRelatedService() != null) {
            List<EncounterR4.Extension> rsChildren = new ArrayList<>();
            addChild(rsChildren, childR4("identifier", in.getData().getRelatedService().getIdentifier()));
            addChild(rsChildren, childR4("name", in.getData().getRelatedService().getName()));
            if (!rsChildren.isEmpty()) {
                EncounterR4.Extension rs = new EncounterR4.Extension(); rs.url = MappingConstants.SD_RELATED_SERVICE; rs.extension = rsChildren; exts.add(rs);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] relatedService.children : {} , valeur", rsChildren.size());
            }
        }

        if (in.getClaims() != null) {
            List<EncounterR4.Extension> claimsChildren = new ArrayList<>();
            addChild(claimsChildren, childR4("endpoint", MappingConstants.CLAIMS_ENDPOINT_MEDICAL_WORKFLOW_STATUS));
            addChild(claimsChildren, childR4("method", in.getClaims().getMethod()));
            addChild(claimsChildren, childR4("class", in.getClaims().getClassName()));
            if (!claimsChildren.isEmpty()) {
                EncounterR4.Extension claims = new EncounterR4.Extension(); claims.url = MappingConstants.SD_CLAIMS; claims.extension = claimsChildren; exts.add(claims);
                log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] claims.children : {} , valeur", claimsChildren.size());
            }
        }

        log.info("[AmbulatoryEncounterMapper] [buildEncounterR4Extensions] end.size : {} , valeur", exts.size());
        return exts;
    }

    private static void addExt(List<EncounterR5.Extension> list, EncounterR5.Extension e) {
        if (e != null) { list.add(e); log.info("[AmbulatoryEncounterMapper] [addExt] added : {} , valeur", safeToString(e.url)); }
    }

    private static void addChild(List<EncounterR5.Extension> list, EncounterR5.Extension e) {
        if (e != null) { list.add(e); log.info("[AmbulatoryEncounterMapper] [addChild] added : {} , valeur", safeToString(e.url)); }
    }

    private static void addExt(List<EncounterR4.Extension> list, EncounterR4.Extension e) {
        if (e != null) { list.add(e); log.info("[AmbulatoryEncounterMapper] [addExt-R4] added : {} , valeur", safeToString(e.url)); }
    }

    private static void addChild(List<EncounterR4.Extension> list, EncounterR4.Extension e) {
        if (e != null) { list.add(e); log.info("[AmbulatoryEncounterMapper] [addChild-R4] added : {} , valeur", safeToString(e.url)); }
    }

    private static EncounterR5.Extension simpleExt(String url, String value) {
        if (value == null || value.isBlank()) return null;
        EncounterR5.Extension e = new EncounterR5.Extension(); e.url = url; e.valueString = value; return e;
    }

    private static EncounterR5.Extension child(String url, String value) {
        if (value == null || value.isBlank()) return null;
        EncounterR5.Extension e = new EncounterR5.Extension(); e.url = url; e.valueString = value; return e;
    }

    private static EncounterR5.Extension child(String url, Boolean value) {
        if (value == null) return null;
        EncounterR5.Extension e = new EncounterR5.Extension(); e.url = url; e.valueBoolean = value; return e;
    }

    private static EncounterR5.Extension child(String url, Integer value) {
        if (value == null) return null;
        EncounterR5.Extension e = new EncounterR5.Extension(); e.url = url; e.valueInteger = value; return e;
    }

    private static EncounterR5.Extension child(String url, Long value) {
        if (value == null) return null;
        EncounterR5.Extension e = new EncounterR5.Extension(); e.url = url; e.valueInteger = Math.toIntExact(value); return e;
    }

    private static EncounterR5.Extension childDateTime(String url, String dateTimeIso) {
        if (dateTimeIso == null || dateTimeIso.isBlank()) return null;
        EncounterR5.Extension e = new EncounterR5.Extension(); e.url = url; e.valueDateTime = dateTimeIso; return e;
    }

    private static EncounterR4.Extension simpleExtR4(String url, String value) {
        if (value == null || value.isBlank()) return null;
        EncounterR4.Extension e = new EncounterR4.Extension(); e.url = url; e.valueString = value; return e;
    }

    private static EncounterR4.Extension childR4(String url, String value) {
        if (value == null || value.isBlank()) return null;
        EncounterR4.Extension e = new EncounterR4.Extension(); e.url = url; e.valueString = value; return e;
    }

    private static EncounterR4.Extension childR4(String url, Boolean value) {
        if (value == null) return null;
        EncounterR4.Extension e = new EncounterR4.Extension(); e.url = url; e.valueBoolean = value; return e;
    }

    private static EncounterR4.Extension childR4(String url, Long value) {
        if (value == null) return null;
        EncounterR4.Extension e = new EncounterR4.Extension(); e.url = url; e.valueInteger = Math.toIntExact(value); return e;
    }

    private static EncounterR4.Extension childDateTimeR4(String url, String dateTimeIso) {
        if (dateTimeIso == null || dateTimeIso.isBlank()) return null;
        EncounterR4.Extension e = new EncounterR4.Extension(); e.url = url; e.valueDateTime = dateTimeIso; return e;
    }

    private static String safeToString(Object value) {
        if (value == null) return "null";
        try { return String.valueOf(value); } catch (Exception e) { return value.getClass().getSimpleName(); }
    }

    private static String safeStr(String s) { return s == null ? null : s; }
}
