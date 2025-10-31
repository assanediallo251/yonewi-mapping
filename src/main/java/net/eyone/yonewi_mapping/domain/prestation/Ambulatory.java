package net.eyone.yonewi_mapping.domain.prestation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eyone.yonewi_mapping.domain.prestation.dtos.DataDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ambulatory {
    private String messageType;
    private String messageKey;
    private String organismId;
    private String organismName;
    private String action;
    private String createdBy;
    private String creationDate;
    private DataDto data;
    private String origin;
}
