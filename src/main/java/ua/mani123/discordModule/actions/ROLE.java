package ua.mani123.discordModule.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import ua.mani123.config.Objects.DiscordConfigs;
import ua.mani123.discordModule.TempData;

import java.util.ArrayList;
import java.util.Objects;

public class ROLE extends DiscordConfigs.Action {

    ArrayList<String> rolesIds;
    boolean remove;
    boolean removeIfHave;
    boolean useTempDataRoles;
    boolean replace;

    public ROLE(String type, CommentedConfig config) {
        super(type, config.getOrElse("id", "not_set"));
        this.rolesIds = config.getOrElse("rolesIds", new ArrayList<>());
        this.remove = config.getOrElse("remove", false);
        this.removeIfHave = config.getOrElse("removeIfHave", false);
        this.useTempDataRoles = config.getOrElse("useTempDataRoles", false);
        this.replace = config.getOrElse("replace", false);
    }

    @Override
    public void run(GenericEvent event, TempData tempData) {
        if (event instanceof Interaction interaction && interaction.isFromGuild()) {
            ArrayList<Role> allRoles = new ArrayList<>();
            if (useTempDataRoles) allRoles.addAll(tempData.getRoles());
            allRoles.addAll(rolesIds.stream().map(id -> {
                try {
                    return Objects.requireNonNull(interaction.getGuild()).getRoleById(id);
                } catch (NullPointerException e) {
                    getLogger().warn("Role with id: " + id + " not found");
                    return null;
                }
            }).toList());
            if (replace) {
                Objects.requireNonNull(interaction.getGuild()).modifyMemberRoles(Objects.requireNonNull(interaction.getMember()), allRoles).queue();
            } else {
                if (remove) {
                    allRoles.forEach(role -> Objects.requireNonNull(interaction.getGuild()).removeRoleFromMember(Objects.requireNonNull(interaction.getMember()), role).queue());
                } else {
                    allRoles.forEach(role -> {
                        if (!Objects.requireNonNull(interaction.getMember()).getRoles().contains(role))
                            Objects.requireNonNull(interaction.getGuild()).addRoleToMember(Objects.requireNonNull(interaction.getMember()), role).queue();
                        else if (removeIfHave)
                            Objects.requireNonNull(interaction.getGuild()).removeRoleFromMember(Objects.requireNonNull(interaction.getMember()), role).queue();
                    });
                }
            }
        }
    }
}
