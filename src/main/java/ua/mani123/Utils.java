package ua.mani123;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;

public class Utils {

  public static Logger initLogger() {
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
    logEncoder.setContext(loggerContext);
    logEncoder.setPattern("%d{HH:mm:ss.SSS} %boldCyan(%-34.-34thread) %red(%10.10X{jda.shard}) %boldGreen(%-15.-15logger{0}) %highlight(%-6level) %msg%n");
    logEncoder.start();

    ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<>();
    logConsoleAppender.setContext(loggerContext);
    logConsoleAppender.setName("console");
    logConsoleAppender.setEncoder(logEncoder);
    logConsoleAppender.start();

    Logger log = loggerContext.getLogger("Main");
    log.setAdditive(false);
    log.setLevel(Level.INFO);
    log.addAppender(logConsoleAppender);
    return log;
  }

}