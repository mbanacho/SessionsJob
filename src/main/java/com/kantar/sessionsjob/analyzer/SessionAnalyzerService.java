package com.kantar.sessionsjob.analyzer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Log4j2
@AllArgsConstructor
public class SessionAnalyzerService {

    private final WriteSessionFileService writeSessionFileService;
    private final ReadSessionFileService readSessionFileService;

    public void transform(String inputFile, String outputFile) {
        Map<Long, List<SessionRecordInput>> sessionsRecordsByHomeNo = readSessionFileService.getSessionsRecordsByHomeNo(inputFile);
        List<SessionRecordOutput> transformedSessionRecords = transformSessionsRecords(sessionsRecordsByHomeNo);
        writeSessionFileService.writeSessionRecords(outputFile, transformedSessionRecords);
    }

    private List<SessionRecordOutput> transformSessionsRecords(Map<Long, List<SessionRecordInput>> sessionsByHomeNo) {
        return sessionsByHomeNo
            .values()
            .stream()
            .flatMap(session -> analyzeSession(session).stream())
            .collect(Collectors.toList());
    }

    private List<SessionRecordOutput> analyzeSession(List<SessionRecordInput> sessionRecords) {
        if(sessionRecords.size() == 1){
            return Collections.singletonList(createMarginalOutputSessionRow(sessionRecords.get(0)));
        } else {
            final List<SessionRecordInput> sortedInputSessionRecords = sessionRecords.stream()
                .sorted(Comparator.comparing(SessionRecordInput::getStartTime))
                .collect(Collectors.toList());

            List<SessionRecordOutput> sessionRecordsOutputs = IntStream.range(0, sortedInputSessionRecords.size() - 1)
                .mapToObj(i -> analyzeRecords(sortedInputSessionRecords.get(i), sortedInputSessionRecords.get(i + 1)))
                .collect(Collectors.toList());

            sessionRecordsOutputs.add(createMarginalOutputSessionRow(sortedInputSessionRecords.get(sortedInputSessionRecords.size() - 1)));
            return sessionRecordsOutputs;
        }
    }

    private SessionRecordOutput analyzeRecords(SessionRecordInput firstSessionRecord, SessionRecordInput secondSessionRecord) {
        final LocalDateTime secondSessionRecordStartTime = secondSessionRecord.getStartTime();
        final LocalDateTime firstSessionRecordStartTime = firstSessionRecord.getStartTime();

        if(firstSessionRecordStartTime.getDayOfYear() == secondSessionRecordStartTime.getDayOfYear()
                && firstSessionRecordStartTime.getYear() == secondSessionRecordStartTime.getYear()){

            LocalDateTime endTime = secondSessionRecordStartTime.minusSeconds(1);
            long duration = getDurationInSeconds(firstSessionRecordStartTime, secondSessionRecordStartTime);
            return new SessionRecordOutput(firstSessionRecord, endTime, duration);
        } else {
            return createMarginalOutputSessionRow(firstSessionRecord);
        }
    }

    private SessionRecordOutput createMarginalOutputSessionRow(SessionRecordInput recordInput) {
        LocalDateTime endTime = recordInput.getStartTime().toLocalDate().atTime(LocalTime.MAX);
        log.debug("Calculated end time {}: ", endTime);
        return new SessionRecordOutput(recordInput, endTime, getDurationInSeconds(recordInput.getStartTime(), endTime.plusNanos(1)));
    }

    private long getDurationInSeconds(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return dateFrom.until(dateTo, ChronoUnit.SECONDS);
    }
}
