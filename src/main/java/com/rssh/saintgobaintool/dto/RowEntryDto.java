package com.rssh.saintgobaintool.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class RowEntryDto {

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

    private MultipartFile image;
}