package com.kantar.sessionsjob.analyzer;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Log4j2
class WriteSessionFileService {

    private static final char PSV_SEPARATOR = '|';

    void writeSessionRecords(String outputFile, List<SessionRecordOutput> sessionRecordOutputs) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(outputFile))) {
            writer.append("homeNo|channel|startTime|activity|endTime|duration \n"); //TODO replace with custom mappingStrategy
            StatefulBeanToCsv<SessionRecordOutput> csvWriter = new StatefulBeanToCsvBuilder<SessionRecordOutput>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withOrderedResults(true)
                .withSeparator(PSV_SEPARATOR)
                .build();
            csvWriter.write(sessionRecordOutputs);
        } catch (IOException e) {
            log.error("Exception while creating PSV file: {}", e.getMessage() );
        } catch (CsvRequiredFieldEmptyException e) {
            log.error("Missing value while creating PSV file, {}", e.getMessage() );
        } catch (CsvDataTypeMismatchException e) {
            log.error("Type mismatch while creating PSV file, {}", e.getMessage() );
        }
    }
}
