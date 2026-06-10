package com.rssh.saintgobaintool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowEntryResponse {

    private Integer sNo;
    private LocalDate date;
    private String day;
    private String contractorName;
    private String phoneNumber;
    private String siteType;
    private String location;
    private String company;
    private String reasonOfUsing;
    private String projectStage;
    private String additionalDescription;

    private String imageUrl;
}