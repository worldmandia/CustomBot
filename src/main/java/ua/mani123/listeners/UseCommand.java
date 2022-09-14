package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.command.CommandUtils;
import ua.mani123.command.CustomCommand;
import ua.mani123.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class UseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CustomCommand cmd = CommandUtils.getAllCommands().get(event.getName());
        Member member = event.getInteraction().getMember();
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put("%username-mentioned%", member.getAsMention());
        placeholders.put("%username%", member.getEffectiveName());
        placeholders.put("%counter%", String.valueOf(0));
        for (Action action : cmd.getActions()) {
            if (action.isOnlyTicket()) {
                DTBot.getLogger().warn(action.getId() + " this action only for tickets");
            } else {
                RestAction<?> restAction = UniversalActionExecutor.use(event, action, placeholders);
                restAction.queue((success) -> event.replyEmbeds(new EmbedBuilder().setTitle(Utils.placeholder(DTBot.getLang().get("command.success-title"), placeholders)).setDescription("Success buttonEvent").build()).setEphemeral(DTBot.getLang().getOrElse("command.ephemeral", true)).queue(),
                        (error) -> DTBot.getLogger().error(error.getMessage()));
            }
        }
    }
}
