package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgrammationTypeDto {
    private String code;
    private String label;
    private Boolean notScheduled;
    private Boolean emergency;
    private Boolean scheduled;
}
