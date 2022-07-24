package ua.mani123.ticket;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import ua.mani123.DTBot;
import ua.mani123.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TicketButton implements Ticket {
    String id;
    String title;
    String description;
    String embedColor;
    String category;
    List<String> buttonIds;

    public TicketButton(String id, String title, String description, String embedColor, String category, List<String> buttonIds) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.embedColor = embedColor;
        this.category = category;
        this.buttonIds = buttonIds;
    }

    public MessageEmbed getEmbed() {
        return new EmbedBuilder().setAuthor(title).setDescription(description).setColor(Utils.decode(embedColor)).build();
    }

    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        for (String id : buttonIds) {
            for (CommentedConfig cfg : DTBot.getButton().getList("button")) {
                if (cfg.get("button-id").equals(id)) {
                    buttons.add(Button.of(
                            ButtonStyle.valueOf(cfg.getOrElse("button-style", "SUCCESS")),
                            cfg.getOrElse("button-id", "0"),
                            cfg.getOrElse("button-text", "Not found text in button")));
                } else {
                    DTBot.getLOGGER().warn("Button with id: " + id + "not found");
                }
            }
        }
        return buttons;
    }

    public String getId() {
        return id;
    }
}
