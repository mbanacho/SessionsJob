package com.kantar.sessionsjob.analyzer;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionAnalyzerServiceTest {

    private final WriteSessionFileService writeSessionFileService =
        new WriteSessionFileService('|',"HomeNo|Channel|Starttime|Activity|EndTime|Duration\n");
    private final ReadSessionFileService readSessionFileService = new ReadSessionFileService('|');
    SessionAnalyzerService testedClass = new SessionAnalyzerService(writeSessionFileService, readSessionFileService);

    @Test
    void givenInput_shouldWriteExpectedValue(){
        //GIVEN
        String inputPath = "src/test/resources/input-statements.psv";
        String outputPath = "src/test/resources/output-statements.psv";
        String expectedSessionsFile = "src/test/resources/expected-sessions.psv";
        //WHEN
        testedClass.transform(inputPath, outputPath);
        //THEN
       // FileAssert
        try {
            final List<String> result = FileUtils.readLines(new File(outputPath), StandardCharsets.UTF_8);
            final List<String> expected = FileUtils.readLines(new File(expectedSessionsFile), StandardCharsets.UTF_8);

            assertTrue(result.containsAll(expected));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void notExistingFile_shouldCallIllegalStateExceptionWithExpectedMessage(){
        //GIVEN
        String inputPath = "src/test/resources/notExistingFile.psv";
        String outputPath = "target/actual-sessions.psv";
        //WHEN
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            testedClass.transform(inputPath, outputPath);
        });
        //THEN
        String expectedMessage = "Error while reading psv file";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}