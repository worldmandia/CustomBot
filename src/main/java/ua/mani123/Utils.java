package ua.mani123;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import java.util.HashMap;
import java.util.Objects;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
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

  public static HashMap<String, String> getPlaceholders(GenericInteractionCreateEvent event) {
    return new HashMap<>(updateEventPlaceholders(event));
  }

  public static HashMap<String, String> updateEventPlaceholders(GenericInteractionCreateEvent event) {
    HashMap<String, String> placeholders = new HashMap<>();
    placeholders.put("interaction-user", event.getUser().getName());
    placeholders.put("interaction-user-mentioned", event.getUser().getAsMention());
    placeholders.put("interaction-user-as-tag", event.getUser().getAsTag());
    placeholders.put("guild-name", Objects.requireNonNull(event.getGuild()).getName());
    placeholders.put("guild-owner-mentioned", Objects.requireNonNull(event.getGuild().getOwner()).getAsMention());
    placeholders.put("guild-owner-nickname", event.getGuild().getOwner().getNickname());
    placeholders.put("channel-mentioned", Objects.requireNonNull(event.getChannel()).getAsMention());
    placeholders.put("channel-type", event.getInteraction().getChannelType().toString());

    return placeholders;
  }

}