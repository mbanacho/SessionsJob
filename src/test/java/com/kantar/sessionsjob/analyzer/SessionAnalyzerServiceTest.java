package com.kantar.sessionsjob.analyzer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SessionAnalyzerServiceTest {

    private final WriteSessionFileService writeSessionFileService = mock(WriteSessionFileService.class);
    private final ReadSessionFileService readSessionFileService = new ReadSessionFileService();
    SessionAnalyzerService testedClass = new SessionAnalyzerService(writeSessionFileService, readSessionFileService);

    @Test
    void correctInput_shouldCallWriteServiceOnes(){
        //GIVEN
        String inputPath = "src/test/resources/input-statements.psv";
        String outputPath = "target/actual-sessions.psv";
        //WHEN
        testedClass.transform(inputPath, outputPath);
        //THEN
        verify(writeSessionFileService, times(1)).writeSessionRecords(Mockito.anyString(), Mockito.anyList());
    }

    @Test
    void notExistingFile_shouldCallWriterWithEmptyList(){
        //GIVEN
        String inputPath = "src/test/resources/notExistingFile.psv";
        String outputPath = "target/actual-sessions.psv";
        //WHEN
        testedClass.transform(inputPath, outputPath);
        //THEN
        verify(writeSessionFileService, times(1)).writeSessionRecords(Mockito.anyString(), eq(Collections.emptyList()));
    }
}