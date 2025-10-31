package net.eyone.yonewi_mapping.domain.prestation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
    private String url;
    private String originalUrl;
    private String bucketName;
    private Integer sizeBytes;
    private String fileType;
    private String fileName;
    private String documentType;
    private String documentTypeCode;
    private Integer fileId;
    private Integer entityId;
    private String eyoneExternalId;
}
