package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.Utils;
import ua.mani123.discord.action.Action;

public class SEND_MESSAGE implements Action {

    String message;

    public SEND_MESSAGE(CommentedFileConfig config) {
        this.message = config.getOrElse("message", "null");
    }

    @Override
    public void run(GenericInteractionCreateEvent event) {
        event.getMessageChannel().sendMessage(message).queue();
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        Utils.getPlaceholders().put("message", message);
        event.getMessageChannel().sendMessage(str.replace(message)).queue();
    }
}
