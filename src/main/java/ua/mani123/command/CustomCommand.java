package ua.mani123.command;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CustomCommand {
    String name;
    String description;
    String permission;

    public CustomCommand(String name, String description, String permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public SlashCommandData getCommand() {
        return Commands.slash(name, description);
    }
}
