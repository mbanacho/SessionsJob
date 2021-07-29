package com.kantar.sessionsjob;

import com.kantar.sessionsjob.analyzer.SessionAnalyzerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class SessionAnalyzerRunner implements CommandLineRunner{

    private final SessionAnalyzerService sessionAnalyzerService;

    @Override
    public void run(String... args) {
        if (args.length < 2) {
            log.error("Missing arguments: <input-statements-file> <output-sessions-file>");
            System.exit(1);
        } else if(args.length > 2){
            log.error("Too many arguments - require: <input-statements-file> <output-sessions-file>");
            System.exit(1);
        }
        sessionAnalyzerService.transform(args[0],args[1]);
    }
}
