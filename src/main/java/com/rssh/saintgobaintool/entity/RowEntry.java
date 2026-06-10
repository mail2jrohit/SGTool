package com.rssh.saintgobaintool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class RowEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer sNo;

    LocalDate date;

    String day;

    String contractorName;

    String phoneNumber;

    String siteType;

    String location;

    String company;

    String reasonOfUsing;

    String projectStage;
    //Ongoing, Finishing, Completed

    String additionalDescription;

    @Lob
    @JsonIgnore
    private byte[] image;

    private String imageContentType;
}
