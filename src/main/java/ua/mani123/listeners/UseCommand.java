package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.action.Action;
import ua.mani123.command.CommandUtils;
import ua.mani123.command.CustomCommand;
import ua.mani123.utils.ReplyReason;
import ua.mani123.utils.Utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CustomCommand cmd = CommandUtils.getAllCommands().get(event.getName());
        Member member = event.getInteraction().getMember();
        Map<String, String> placeholders = new HashMap<>();
        ReplyReason replyReason = null;

        placeholders.put("%username-mentioned%", member.getAsMention());
        placeholders.put("%username%", member.getEffectiveName());
        placeholders.put("%username-with-tag%", member.getUser().getAsTag());
        placeholders.put("%data-month%", String.valueOf(LocalDateTime.now().getMonthValue()));
        placeholders.put("%data-day%", String.valueOf(LocalDateTime.now().getDayOfMonth()));
        placeholders.put("%data-hour%", String.valueOf(LocalDateTime.now().getHour()));
        placeholders.put("%data-year%", String.valueOf(LocalDateTime.now().getYear()));
        placeholders.put("%data-minute%", String.valueOf(LocalDateTime.now().getMinute()));
        placeholders.put("%counter%", String.valueOf(0));
        for (Action action : cmd.getActions()) {
            replyReason = UniversalActionExecutor.use(event, action, placeholders);
        }
        if (replyReason.getRestAction() != null) {
            ReplyReason finalReplyReason = replyReason;
            replyReason.getRestAction().queue((success) -> event.replyEmbeds(new EmbedBuilder().setTitle(Utils.placeholder(finalReplyReason.getTitle(), placeholders)).setDescription(finalReplyReason.getDescription()).build()).setEphemeral(finalReplyReason.isEphemeral()).queue());
        }
    }
}
