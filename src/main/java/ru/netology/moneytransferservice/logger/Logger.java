package ru.netology.moneytransferservice.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static java.lang.String.format;

public class Logger {
    private static Logger logger;
    private final static String ANSI_RESET = "\u001B[0m";
    private final static String ANSI_GREEN = "\u001B[32m";
    private final static String ANSI_RED = "\u001B[31m";
    private static FileWriter logFile;


    private Logger() {
    }

    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
            try {
                logFile = new FileWriter("./moneyapp.log");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return logger;
    }

    public void log(LogSeverity severity, Object requestDto, int operationId) {
        final String color = (operationId == -1 ? ANSI_RED : ANSI_GREEN);
        String log = format(
                "[%s] [%-5s] ==> %s %s\n",
                String.valueOf(LocalDateTime.now()).substring(0, 19),
                severity.name(),
                requestDto,
                "operationId: " + (operationId == -1 ? "UNSUCCESSFUL" : "SUCCESSFUL " + operationId)
        );
        System.out.printf(color + log + ANSI_RESET);
        try {
            logFile.append(log);
            logFile.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
