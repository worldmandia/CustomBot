package ua.mani123.discord.interaction;

import com.electronwill.nightconfig.core.CommentedConfig;
import ua.mani123.discord.interaction.interactions.ButtonInteraction;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

import java.util.ArrayList;
import java.util.List;

public class interactionUtils {

    static ArrayList<CommandInteraction> commands = new ArrayList<>();
    static ArrayList<ButtonInteraction> buttons = new ArrayList<>();

    public static void initCmd(List<CommentedConfig> config) {
        for (CommentedConfig cfg : config) {
            commands.add(new CommandInteraction(cfg));
        }
    }

    public static void initButton(List<CommentedConfig> config) {
        for (CommentedConfig cfg : config) {
            buttons.add(new ButtonInteraction(cfg));
        }
    }

    public static CommandInteraction getCommandByName(String name) {
        for (CommandInteraction cmd : commands) {
            if (cmd.getName().equals(name)) {
                return cmd;
            }
        }
        return null;
    }

    public static ArrayList<CommandInteraction> getCommands() {
        return commands;
    }
}
