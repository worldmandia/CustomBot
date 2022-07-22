package ua.mani123.ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class TicketButton implements Ticket{
    String id;
    String title;
    String description;
    String category;
    String buttonStyle;
    String buttonText;
    String buttonEmoji;

    public TicketButton(String id, String title, String description, String category, String buttonStyle, String buttonText, String buttonEmoji) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.buttonStyle = buttonStyle;
        this.buttonText = buttonText;
        this.buttonEmoji = buttonEmoji;
    }

    public MessageEmbed getEmbed(){
        return new EmbedBuilder().setAuthor(title).setDescription(description).build();
    }

    public Button getButton(){
        return Button.of(ButtonStyle.valueOf(buttonStyle), buttonText, buttonEmoji);
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }
}
