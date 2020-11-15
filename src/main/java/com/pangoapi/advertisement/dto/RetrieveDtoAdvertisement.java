package com.pangoapi.advertisement.dto;

import com.pangoapi.advertisement.entity.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;


@AllArgsConstructor
@Builder
@Getter
public class RetrieveDtoAdvertisement {

    private Long id;
    private String userEmail;
    private String title;
    private String detailDescription;
    private String advertisementType;
    private String advertisementWidth;
    private String advertisementHeight;
    private String imagePath;
    private String siteUrl;
    private String rowPosition;
    private String columnPosition;
    private char isActivated;
    private String startedDate;
    private String finishedDate;

    public static RetrieveDtoAdvertisement createRetrieveDtoAdvertisement(Advertisement advertisement) {
        return RetrieveDtoAdvertisement.builder()
                .id(advertisement.getId())
                .userEmail(advertisement.getUser() != null ? advertisement.getUser().getEmail() : "")
                .title(advertisement.getTitle())
                .detailDescription(advertisement.getDetailDescription())
                .advertisementType(advertisement.getAdvertisementType().getType())
                .advertisementWidth(advertisement.getAdvertisementType().getWidth())
                .advertisementHeight(advertisement.getAdvertisementType().getHeight())
                .imagePath(advertisement.getImagePath())
                .siteUrl(advertisement.getSiteUrl())
                .rowPosition(advertisement.getRowPosition())
                .columnPosition(advertisement.getColumnPosition())
                .isActivated(advertisement.getIsActivated())
                .startedDate(advertisement.getStartedDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .finishedDate(advertisement.getFinishedDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .build();
    }
}