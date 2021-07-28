package com.kantar.sessionsjob.analyzer;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@Log4j2
class ReadSessionFileService {

    private static final char PSV_SEPARATOR = '|';

    Map<Long, List<SessionRecordInput>> getSessionsRecordsByHomeNo(String inputFile) {
        final Path absolutePath = Paths.get(new File(inputFile).getAbsolutePath());
        log.debug("Read session info from file: {}", inputFile);
        Map<Long, List<SessionRecordInput>> sessionRecordInputList = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(absolutePath)) {
            CsvToBean<SessionRecordInput> csvToBean = new CsvToBeanBuilder(reader)
                .withSeparator(PSV_SEPARATOR)
                .withType(SessionRecordInput.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
            sessionRecordInputList = csvToBean.stream().collect(groupingBy(SessionRecordInput::getHomeNo));
        } catch (IOException e) {
            log.error("Error while reading psv file, {}", e.getMessage());
        }
        return sessionRecordInputList;
    }
}
