package net.eyone.yonewi_mapping.domain.prestation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eyone.yonewi_mapping.domain.prestation.dtos.analysis.AnalysisDataDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Analysis {
    private String messageType;
    private String messageKey;
    private String organismId;
    private String organismName;
    private String action;
    private String createdBy;
    private String creationDate;
    private AnalysisDataDto data;
    private String origin;
    private String status;
}
