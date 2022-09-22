package ua.mani123.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.action.UniversalActionExecutor;
import ua.mani123.action.botAction;
import ua.mani123.interaction.botInteraction;
import ua.mani123.interaction.interactions.BUTTON_INTERACTION;
import ua.mani123.interaction.interactions.InteractionUtils;
import ua.mani123.utils.ReplyReason;
import ua.mani123.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ButtonEvents extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        botInteraction interaction = InteractionUtils.getAllInteractions().get(event.getComponentId());
        Member member = event.getMember();
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put("%username-mentioned%", member.getAsMention());
        placeholders.put("%username%", member.getEffectiveName());
        placeholders.put("%username-with-tag%", member.getUser().getAsTag());
        placeholders.put("%counter%", String.valueOf(0));
        ReplyReason replyReason = null;

        if (interaction instanceof BUTTON_INTERACTION button_interaction) {
            for (botAction action : button_interaction.getActions()) {
                if (action.isOnlyCommand()) {
                    DTBot.getLogger().warn(action.getId() + " this action only for commands");
                } else {
                    if (!UniversalActionExecutor.nextSkip){
                        replyReason = UniversalActionExecutor.use(event, action, placeholders);
                        UniversalActionExecutor.clear();
                        UniversalActionExecutor.nextSkip = false;
                    }
                    if (replyReason.getRestAction() != null) {
                        ReplyReason finalReplyReason = replyReason;
                        replyReason.getRestAction().queue((success) -> event.replyEmbeds(new EmbedBuilder().setTitle(Utils.placeholder(finalReplyReason.getTitle(), placeholders)).setDescription(finalReplyReason.getDescription()).build()).setEphemeral(finalReplyReason.isEphemeral()).queue());
                    }
                }
            }
            //event.replyEmbeds(new EmbedBuilder().setTitle(Utils.placeholder(replyReason.getTitle(), placeholders)).setDescription(replyReason.getDescription()).build()).setEphemeral(replyReason.isEphemeral()).queue();
        }
    }
}
