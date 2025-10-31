package net.eyone.yonewi_mapping.model.output;

import java.util.List;

public class ServiceRequestR4 {
    public String resourceType;
    public String id;
    public Meta meta;
    public Text text;
    public List<Extension> extension;
    public List<Identifier> identifier;
    public String status;
    public String intent;
    public List<CodeableConcept> category;
    public String priority; // simple code value in sample JSON ("routine")
    public CodeableConcept code;
    public Reference subject;
    public String authoredOn;
    public Reference requester;
    public List<Reference> performer;
    public List<Reference> locationReference;
    public Period occurrencePeriod;
    public List<Note> note;

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
        public Double valueDecimal;
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

    public static class Period {
        public String start;
        public String end;
    }

    public static class Reference {
        public String reference;
        public Identifier identifier;
        public String display;
    }

    public static class Note {
        public String authorString;
        public String time;
        public String text;
    }
}
