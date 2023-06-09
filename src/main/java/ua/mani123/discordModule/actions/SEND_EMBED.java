package ua.mani123.discordModule.actions;

import lombok.Getter;
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
public class SEND_EMBED extends DiscordConfigs.Action {

    private final MessageEmbed messageEmbed;
    private final String url;
    private final String title;
    private final String description;
    private final TemporalAccessor timestamp;
    private final String color;
    private final String thumbnail;
    private final String author;
    private final String footer;
    private final String image;
    private final boolean reply;
    private final boolean ephemeral;
    private final List<MessageEmbed.Field> fields;

    public SEND_EMBED(String type, String id, String url, String title, String description, String timestamp, String color, String thumbnail, String author, String footer, String image, List<MessageEmbed.Field> fields, boolean reply, boolean ephemeral) {
        super(type, id);
        this.url = url;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp.equalsIgnoreCase("NOW") ? Instant.now() : DateTimeFormatter.ISO_LOCAL_DATE.parse(timestamp);
        this.color = color;
        this.thumbnail = thumbnail;
        this.author = author;
        this.footer = footer;
        this.image = image;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (!url.equals("")) embedBuilder.setUrl(this.url);
        if (!title.equals("")) embedBuilder.setTitle(this.title);
        if (!description.equals("")) embedBuilder.setDescription(this.description);
        if (!timestamp.equals("")) embedBuilder.setTimestamp(this.timestamp);
        if (!color.equals("")) embedBuilder.setColor(Color.decode(this.color));
        if (!thumbnail.equals("")) embedBuilder.setThumbnail(this.thumbnail);
        if (!author.equals("")) embedBuilder.setAuthor(this.author);
        if (!footer.equals("")) embedBuilder.setFooter(this.footer);
        if (!image.equals("")) embedBuilder.setImage(this.image);
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
                getLogger().error("SEND_EMBED cant send message to non TextChannel");
            }
        } else {
            getLogger().error("SEND_EMBED cant reply or send message");
        }
    }
}