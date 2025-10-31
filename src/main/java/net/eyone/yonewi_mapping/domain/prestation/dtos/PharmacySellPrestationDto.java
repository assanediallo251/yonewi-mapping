package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PharmacySellPrestationDto {
    private Integer identifier;
    private Boolean disable;
    private Integer totalPrice;
    private Integer nbrProducts;
    private String commentary;
    private PharmacyBillingModeDto pharmacyBillingMode;
    private CareDto care;
    private Boolean usedAsAPrestation;
    private Boolean useComplementaryCare;
    private Boolean manageStock;
    private Boolean documentGenerationRequired2;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PharmacyBillingModeDto {
        private String code;
        private String label;
        private String scope;
        private Boolean global;
        private Boolean detail;
    }
}
