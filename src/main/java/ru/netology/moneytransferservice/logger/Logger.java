package ru.netology.moneytransferservice.logger;

import java.time.LocalDateTime;

import static java.lang.String.format;

public class Logger {
    private static Logger logger;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    private Logger() {
    }

    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public void log(LogSeverity severity, Object requestDto, String operationId) {
        final String color = (operationId == null ? ANSI_RED : ANSI_GREEN);
        String log = format(
                color + "[%s] [%-5s] ==> %s %s\n" + ANSI_RESET,
                String.valueOf(LocalDateTime.now()).substring(0, 19),
                severity.name(),
                requestDto,
                "operationId: " + (operationId == null ? "UNSUCCESSFUL" : "SUCCESSFUL " + operationId)
        );
        System.out.printf(log);
    }
}
