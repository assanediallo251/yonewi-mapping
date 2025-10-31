package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {
    private String code;
    private String label;
    private Boolean done;
    private Boolean created;
    private Boolean cancelled;
    private Boolean toBeConfirmed;
    private Boolean notAccepted;
    private Boolean onGoing;
    private Boolean unavailable;
    private Boolean waiting;
    private Boolean notPerformed;
}
