package ua.mani123.discordModule.actions;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import ua.mani123.config.Objects.DiscordConfigs;

import java.awt.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Getter
@Setter
public class SEND_EMBED extends DiscordConfigs.Action {

    private MessageEmbed messageEmbed;
    private String url;
    private String title;
    private String description;
    private String timestamp;
    private String color;
    private String thumbnail;
    private String author;
    private String footer;
    private String image;
    private boolean reply;
    private boolean ephemeral;
    private List<MessageEmbed.Field> fields;

    public SEND_EMBED(String type, String id, String url, String title, String description, String timestamp, String color, String thumbnail, String author, String footer, String image, List<MessageEmbed.Field> fields, boolean reply, boolean ephemeral) {
        super(type, id);
        this.url = url;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.color = color;
        this.thumbnail = thumbnail;
        this.author = author;
        this.footer = footer;
        this.image = image;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (url != null) embedBuilder.setUrl(this.url);
        if (title != null) embedBuilder.setTitle(this.title);
        if (description != null) embedBuilder.setDescription(this.description);
        if (timestamp != null) embedBuilder.setTimestamp(timestamp.equalsIgnoreCase("NOW") ? Instant.now() : parseTimeStamp(timestamp));
        if (color != null) embedBuilder.setColor(Color.decode(this.color));
        if (thumbnail != null) embedBuilder.setThumbnail(this.thumbnail);
        if (author != null) embedBuilder.setAuthor(this.author);
        if (footer != null) embedBuilder.setFooter(this.footer);
        if (image != null) embedBuilder.setImage(this.image);
        this.messageEmbed = embedBuilder.build();
        this.reply = reply;
        this.ephemeral = ephemeral;
        this.fields = fields; // TODO soon
    }

    @Override
    public void run(GenericEvent event) {
        if (event instanceof IReplyCallback replyCallback && reply) {
            replyCallback.replyEmbeds(messageEmbed).setEphemeral(ephemeral).queue();
        } else if (event instanceof Interaction interaction) {
            if (interaction.getChannel() instanceof TextChannel textChannel) {
                textChannel.sendMessageEmbeds(messageEmbed).queue();
            } else {
                getLogger().error(getId() + " cant send message to non TextChannel");
            }
        } else {
            getLogger().error(getId() + " cant reply or send message");
        }
    }

    public TemporalAccessor parseTimeStamp(String s) {
        if (s != null) {
            return DateTimeFormatter.ISO_LOCAL_DATE.parse(s);
        } else {
            return Instant.now();
        }
    }
}
