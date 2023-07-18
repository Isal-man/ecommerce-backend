package com.learn.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long size;

}
