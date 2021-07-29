package com.kantar.sessionsjob.analyzer;

import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
class WriteSessionFileService {

    @Value("${psv.separator}")
    private char psvSeparator;

    @Value("${psv.output.header}")
    private String psvOutputHeader;

    void writeSessionRecords(String outputFile, List<SessionRecordOutput> sessionRecordOutputs) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(outputFile))) {
            writer.append(psvOutputHeader);
            StatefulBeanToCsv<SessionRecordOutput> csvWriter = new StatefulBeanToCsvBuilder<SessionRecordOutput>(writer)
                .withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
                .withOrderedResults(true)
                .withSeparator(psvSeparator)
                .build();
            csvWriter.write(sessionRecordOutputs);
        } catch (IOException e) {
            throw new IllegalStateException("Exception while creating PSV file", e);
        } catch (CsvRequiredFieldEmptyException e) {
            throw new IllegalStateException("Missing value while creating PSV file", e);
        } catch (CsvDataTypeMismatchException e) {
            throw new IllegalStateException("Type mismatch while creating PSV file", e);
        }
    }
}
