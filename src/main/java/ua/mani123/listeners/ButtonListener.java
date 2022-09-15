package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.interactions.BUTTON_INTERACTION;
import ua.mani123.interaction.interactions.InteractionUtils;
import ua.mani123.utils.ReplyReason;
import ua.mani123.utils.Utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Interaction interaction = InteractionUtils.getAllInteractions().get(event.getComponentId());
        Member member = event.getMember();
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put("%username-mentioned%", member.getAsMention());
        placeholders.put("%username%", member.getEffectiveName());
        placeholders.put("%username-with-tag%", member.getUser().getAsTag());
        placeholders.put("%data-month%", String.valueOf(LocalDateTime.now().getMonthValue()));
        placeholders.put("%data-day%", String.valueOf(LocalDateTime.now().getDayOfMonth()));
        placeholders.put("%data-hour%", String.valueOf(LocalDateTime.now().getHour()));
        placeholders.put("%data-year%", String.valueOf(LocalDateTime.now().getYear()));
        placeholders.put("%data-minute%", String.valueOf(LocalDateTime.now().getMinute()));
        placeholders.put("%counter%", String.valueOf(0));
        ReplyReason replyReason = null;

        if (interaction instanceof BUTTON_INTERACTION button_interaction) {
            for (Action action : button_interaction.getActions()) {
                if (action.isOnlyCommand()) {
                    DTBot.getLogger().warn(action.getId() + " this action only for commands");
                } else {
                    replyReason = UniversalActionExecutor.use(event, action, placeholders);
                    if (replyReason.getRestAction() != null){
                        replyReason.getRestAction().queue();
                    }
                }
            }
            event.replyEmbeds(new EmbedBuilder().setTitle(Utils.placeholder(replyReason.getTitle(), placeholders)).setDescription(replyReason.getDescription()).build()).setEphemeral(replyReason.isEphemeral()).queue();
        }
    }
}
