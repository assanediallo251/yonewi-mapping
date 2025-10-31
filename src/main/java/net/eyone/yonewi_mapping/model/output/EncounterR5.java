package net.eyone.yonewi_mapping.model.output;

import java.util.List;

public class EncounterR5 {
    public String resourceType;
    public String id;
    public Meta meta;
    public Text text;
    public List<Extension> extension;
    public List<Identifier> identifier;
    public String status;
    public List<EncounterClass> encounterClass; // represents JSON field "class"
    public CodeableConcept priority;
    public List<CodeableConcept> type;
    public List<ServiceTypeItem> serviceType;
    public Reference subject;
    public CodeableConcept subjectStatus;
    public Period actualPeriod;
    public Reference serviceProvider;
    public List<Participant> participant;
    public List<EncounterLocation> location;

    public static class Meta {
        public String versionId;
        public String lastUpdated;
        public List<String> profile;
    }

    public static class Text {
        public String status;
        public String div;
    }

    public static class Extension {
        public String url;
        public String valueString;
        public Boolean valueBoolean;
        public Integer valueInteger;
        public String valueDateTime;
        public List<Extension> extension;
    }

    public static class Identifier {
        public String system;
        public String value;
    }

    public static class Coding {
        public String system;
        public String code;
        public String display;
    }

    public static class CodeableConcept {
        public List<Coding> coding;
        public String text;
    }

    public static class Concept {
        public List<Coding> coding;
    }

    public static class Period {
        public String start;
        public String end;
    }

    public static class Reference {
        public String reference;
        public Identifier identifier;
        public String display;
    }

    public static class EncounterClass {
        public List<Coding> coding;
    }

    public static class Participant {
        public List<CodeableConcept> type;
        public Period period;
        public Reference actor;
    }

    public static class EncounterLocation {
        public Reference location;
        public Period period;
    }

    public static class ServiceTypeItem {
        public Concept concept;
    }
}
