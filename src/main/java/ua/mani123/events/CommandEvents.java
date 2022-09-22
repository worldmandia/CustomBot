package ua.mani123.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.action.UniversalActionExecutor;
import ua.mani123.action.botAction;
import ua.mani123.command.CommandUtils;
import ua.mani123.command.CustomCommand;
import ua.mani123.utils.ReplyReason;
import ua.mani123.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class CommandEvents extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CustomCommand cmd = CommandUtils.getAllCommands().get(event.getName());
        Member member = event.getInteraction().getMember();
        Map<String, String> placeholders = new HashMap<>();
        ReplyReason replyReason = null;

        placeholders.put("%username-mentioned%", member.getAsMention());
        placeholders.put("%username%", member.getEffectiveName());
        placeholders.put("%username-with-tag%", member.getUser().getAsTag());
        placeholders.put("%counter%", String.valueOf(0));
        for (botAction action : cmd.getActions()) {
            if (!UniversalActionExecutor.nextSkip){
                replyReason = UniversalActionExecutor.use(event, action, placeholders);
                UniversalActionExecutor.clear();
                UniversalActionExecutor.nextSkip = false;
            }
        }
        if (replyReason.getRestAction() != null) {
            ReplyReason finalReplyReason = replyReason;
            replyReason.getRestAction().queue((success) -> event.replyEmbeds(new EmbedBuilder().setTitle(Utils.placeholder(finalReplyReason.getTitle(), placeholders)).setDescription(finalReplyReason.getDescription()).build()).setEphemeral(finalReplyReason.isEphemeral()).queue());
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
    }

}
