package com.kantar.sessionsjob.analyzer;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionRecordOutput {

    @CsvBindByPosition(position = 0)
    private Long homeNo;

    @CsvBindByPosition(position = 1)
    private Integer channel;

    @CsvDate(value = "yyyyMMddHHmmss")
    @CsvBindByPosition(position = 2)
    private LocalDateTime startTime;

    @CsvBindByPosition(position = 3)
    private Activity activity;

    @CsvDate(value = "yyyyMMddHHmmss")
    @CsvBindByPosition(position = 4)
    private LocalDateTime endTime;

    @CsvBindByPosition(position = 5)
    private Long duration;

    public SessionRecordOutput(SessionRecordInput recordInput, LocalDateTime endTime, long duration) {
        this.homeNo = recordInput.getHomeNo();
        this.channel = recordInput.getChannel();
        this.startTime = recordInput.getStartTime();
        this.activity = recordInput.getActivity();
        this.endTime = endTime;
        this.duration = duration;
    }
}
