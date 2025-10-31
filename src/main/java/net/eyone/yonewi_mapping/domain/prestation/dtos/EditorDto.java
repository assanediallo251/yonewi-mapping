package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditorDto {
    private EditorOrganismDto organism;
    private Integer userId;
    private String userName;
    private String completeName;
    private UserDetailsInfosDto userDetailsInfos;
    private Integer identifier;
    private Boolean doctor;
    private String completeNameAndUserName;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EditorOrganismDto {
        private Integer identifier;
        private String name;
        private String eyoneExternalId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDetailsInfosDto {
        private Boolean hasSuperviseurRight;
        private Boolean doctor;
    }
}
