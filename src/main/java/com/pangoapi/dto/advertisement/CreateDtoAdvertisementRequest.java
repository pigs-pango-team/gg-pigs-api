package com.pangoapi.dto.advertisement;

import lombok.Getter;

@Getter
public class CreateDtoAdvertisementRequest {

    private String title;
    private String userEmail;
    private String detailDescription;
    private String advertisementType;
    private String imagePath;
    private String siteUrl;
    private Long rowPosition;
    private Long columnPosition;
}
