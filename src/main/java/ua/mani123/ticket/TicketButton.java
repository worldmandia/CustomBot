package ua.mani123.ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import ua.mani123.utils.Utils;

public class TicketButton implements Ticket{
    String id;
    String title;
    String description;
    String embedColor;
    String category;
    String buttonStyle;
    String buttonId;
    String buttonText;

    public TicketButton(String id, String title, String description, String embedColor, String category, String buttonStyle, String buttonText, String buttonEmoji) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.embedColor = embedColor;
        this.category = category;
        this.buttonStyle = buttonStyle;
        this.buttonId = buttonText;
        this.buttonText = buttonEmoji;
    }

    public MessageEmbed getEmbed(){
        return new EmbedBuilder().setAuthor(title).setDescription(description).setColor(Utils.decode(embedColor)).build();
    }

    public Button getButton(){
        return Button.of(ButtonStyle.valueOf(buttonStyle), buttonId, buttonText);
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }
}
