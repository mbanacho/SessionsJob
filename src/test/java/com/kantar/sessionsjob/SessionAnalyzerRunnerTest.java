package com.kantar.sessionsjob;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import com.kantar.sessionsjob.analyzer.SessionAnalyzerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SessionAnalyzerRunnerTest {

    @Mock
    SessionAnalyzerService sessionAnalyzerService = mock(SessionAnalyzerService.class);
    SessionAnalyzerRunner testedClass = new SessionAnalyzerRunner(sessionAnalyzerService);

    @Test
    @ExpectSystemExitWithStatus(1)
    void runWithoutParameters_shouldFinnishWithSystemExit(){
        testedClass.run();
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    void runWithOneParameters_shouldFinnishWithSystemExit(){
        String[] arg = {"/path/to/one/file.psv"};
        testedClass.run(arg);
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    void runWithThreeParameters_shouldFinnishWithSystemExit(){
        String[] arg = {"/path/to/one/file.psv","/path/to/one/file.psv","/path/to/one/file.psv"};
        testedClass.run(arg);
    }

    @Test
    void runWithTwoParameters_shouldCallService(){
        //GIVEN
        String[] arg = {"/path/to/one/file.psv", "/path/to/one/file.psv"};
        //WHEN
        testedClass.run(arg);
        //THEN
        verify(sessionAnalyzerService, times(1)).transform(Mockito.any(), Mockito.any());
    }

}