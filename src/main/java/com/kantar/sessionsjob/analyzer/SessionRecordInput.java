package com.kantar.sessionsjob.analyzer;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionRecordInput {

    @CsvBindByName(column = "HomeNo")
    private Long homeNo;

    @CsvBindByName(column = "Channel")
    private Integer channel;

    @CsvDate(value = "yyyyMMddHHmmss")
    @CsvBindByName(column = "Starttime")
    private LocalDateTime startTime;

    @CsvBindByName(column = "Activity")
    private Activity activity;
}
