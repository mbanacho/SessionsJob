package com.kantar.sessionsjob.analyzer;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
class ReadSessionFileService {

    @Value("${psv.separator}")
    private char psvSeparator;

    Map<Long, List<SessionRecordInput>> getSessionsRecordsByHomeNo(String inputFile) {
        final Path absolutePath = Paths.get(new File(inputFile).getAbsolutePath());
        log.debug("Read session info from file: {}", inputFile);
        Map<Long, List<SessionRecordInput>> sessionRecordInputList;
        try (Reader reader = Files.newBufferedReader(absolutePath)) {
            CsvToBean<SessionRecordInput> csvToBean = new CsvToBeanBuilder<SessionRecordInput>(reader)
                .withSeparator(psvSeparator)
                .withType(SessionRecordInput.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
            sessionRecordInputList = csvToBean.stream().collect(groupingBy(SessionRecordInput::getHomeNo));
        } catch (IOException e) {
            throw new IllegalStateException("Error while reading psv file", e);
        }
        return sessionRecordInputList;
    }
}
