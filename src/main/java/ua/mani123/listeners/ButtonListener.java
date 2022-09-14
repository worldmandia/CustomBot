package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.interactions.BUTTON_INTERACTION;
import ua.mani123.interaction.interactions.InteractionUtils;
import ua.mani123.utils.Utils;

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
        placeholders.put("%counter%", String.valueOf(0));

        if (interaction instanceof BUTTON_INTERACTION button_interaction) {
            for (Action action : button_interaction.getActions()) {
                if (action.isOnlyCommand()) {
                    DTBot.getLogger().warn(action.getId() + " this action only for commands");
                } else {
                    RestAction<?> restAction = UniversalActionExecutor.use(event, action, placeholders);

                    restAction.queue((success) -> event.replyEmbeds(new EmbedBuilder().setTitle(Utils.placeholder(DTBot.getLang().get("button.success-title"), placeholders)).setDescription("Success buttonEvent").build()).setEphemeral(DTBot.getLang().getOrElse("button.ephemeral", true)).queue(),
                            (error) -> DTBot.getLogger().error(error.getMessage()));
                }
            }
        }
    }
}
