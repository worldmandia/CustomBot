package ua.mani123.discordModule.actions;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Setter
public class SEND_EMBED extends DiscordConfigs.Action {

    private MessageEmbed messageEmbed;
    private String url;
    private String title;
    private String description;
    private TemporalAccessor timestamp;
    private String color;
    private String thumbnail;
    private String author;
    private String footer;
    private String image;
    private boolean reply;
    private boolean ephemeral;
    private List<MessageEmbed.Field> fields;

    public SEND_EMBED(String type, String id, String url, String title, String description, String timestamp, String color, String thumbnail, String author, String footer, String image, List<MessageEmbed.Field> fields, boolean reply, boolean ephemeral) {
        this.setId(id);
        this.setType(type);
        this.url = url;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp.equalsIgnoreCase("NOW") ? Instant.now() : parseTimeStamp(timestamp);
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

    public TemporalAccessor parseTimeStamp(String s) {
        if (!s.equals("")) {
            return DateTimeFormatter.ISO_LOCAL_DATE.parse(s);
        } else {
            return null;
        }
    }
}
