package com.kantar.sessionsjob;

import com.kantar.sessionsjob.analyzer.SessionAnalyzerService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SessionAnalyzerRunner implements CommandLineRunner{

    private final SessionAnalyzerService sessionAnalyzerService;

    @Override
    public void run(String... args) {
        if (args.length < 2) {
            System.err.println("Missing arguments: <input-statements-file> <output-sessions-file>");
            System.exit(1);
        }
        sessionAnalyzerService.transform(args[0],args[1]);
    }
}
