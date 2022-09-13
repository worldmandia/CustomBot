package ua.mani123.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.interactions.BUTTON_INTERACTION;
import ua.mani123.interaction.interactions.InteractionUtils;

import java.util.HashMap;
import java.util.Map;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Interaction interaction = InteractionUtils.getAllInteractions().get(event.getComponentId());
        Member member = event.getInteraction().getMember();
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%username-mentioned%", member.getAsMention());
        placeholders.put("%username%", member.getEffectiveName());
        if (interaction instanceof BUTTON_INTERACTION button_interaction) {
            for (Action action : button_interaction.getActions()) {
                if (action.isOnlyCommand()) {
                    DTBot.getLogger().warn(action.getId() + " this action only for commands");
                } else {
                    UniversalActionExecutor.use(event, action);
                }
            }
        }
    }
}
