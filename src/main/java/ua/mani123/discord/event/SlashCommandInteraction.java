package ua.mani123.discord.event;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ua.mani123.CBot;
import ua.mani123.Utils;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.action.actions.SEND_MESSAGE;
import ua.mani123.discord.interaction.interactionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SlashCommandInteraction extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandInteraction commandInteraction = interactionUtils.getCommandByName(event.getName());
        Utils.getPlaceholders().put("interaction-user", event.getUser().getName());
        Utils.getPlaceholders().put("interaction-user-mentioned", event.getUser().getAsMention());
        Utils.getPlaceholders().put("interaction-user-as-tag", event.getUser().getAsTag());

        StringSubstitutor str = new StringSubstitutor(Utils.getPlaceholders());

        List<String> allowRoles = commandInteraction.getAllowRoles();
        if (!allowRoles.isEmpty()) {
            List<Role> roles = new ArrayList<>();
            for (String name : allowRoles) {
                roles.add(event.getGuild().getRolesByName(name, false).get(0));
            }
            if (!new HashSet<>(event.getMember().getRoles()).containsAll(roles)){
                reply(commandInteraction.getErrorAction(), event, commandInteraction.getConfig().getOrElse("error.IsEphemeral", true));
                return;
            }
        }

        List<String> allowUsers = commandInteraction.getAllowUsers();
        if (!allowUsers.isEmpty()) {
            List<Member> members = new ArrayList<>();
            for (String name : allowUsers) {
                members.add(event.getGuild().getMemberByTag(name));
            }
            if (!members.contains(event.getMember())){
                reply(commandInteraction.getErrorAction(), event, commandInteraction.getConfig().getOrElse("error.IsEphemeral", true));
                return;
            }
        }

        List<String> actionIds = commandInteraction.getActionIds();
        if (actionIds != null) {
            for (String actionId : actionIds) {
                if (actionUtils.getActionMap().containsKey(actionId)) {
                    actionUtils.getActionMap().get(actionId).runWithPlaceholders(event, str);
                } else {
                    CBot.getLog().warn(actionId + " - not found");
                }
            }
        }

        reply(commandInteraction.getSuccessAction(), event, commandInteraction.getConfig().getOrElse("success.IsEphemeral", true));

    }

    private static void reply(Action action, SlashCommandInteractionEvent event, boolean isEphemeral){
        if (action instanceof SEND_MESSAGE send_message) {
            event.reply(send_message.getMessage()).setEphemeral(isEphemeral).queue();
        }
    }

}
