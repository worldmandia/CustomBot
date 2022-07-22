package ua.mani123.Command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import ua.mani123.DTBot;
import ua.mani123.config.BotConfig;

public class RegisterCommands {
    public RegisterCommands(JDA api, BotConfig lang) {
        try {
            api.updateCommands().addCommands(
                    Commands.slash(lang.getString("commands.ticketembed.name", "ticketembed"), lang.getString("commands.embed.description"))
                            .addOption(OptionType.STRING, "type", "type of ticket", true, true)
                            .addOption(OptionType.STRING, "id", "id of ticket", true, true)
            ).addCommands(Commands.slash("test", "test"))
                    .queue();
        } catch (IllegalArgumentException e) {
            DTBot.getLOGGER().error(e.getMessage() + ", in lang you need change cmd ");
        }
    }
}
