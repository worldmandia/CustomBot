package ua.mani123.discordModule.actions;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import ua.mani123.config.Objects.DiscordConfigs;
import ua.mani123.discordModule.TempData;

@Getter
@Setter
public class SEND_MESSAGE extends DiscordConfigs.Action {

    private String message;
    private boolean reply;
    private boolean ephemeral;

    public SEND_MESSAGE(String type, String id, String message, boolean reply, boolean ephemeral) {
        super(type, id);
        this.message = message;
        this.reply = reply;
        this.ephemeral = ephemeral;
    }

    @Override
    public void run(GenericEvent event, TempData tempData) {
        if (!message.equals("")) {
            getLogger().error(getId() + "cant send empty message");
            return;
        }
        if (event instanceof IReplyCallback replyCallback && reply) {
            replyCallback.reply(message).setEphemeral(ephemeral).queue();
        } else if (event instanceof Interaction interaction) {
            if (interaction.getChannel() instanceof TextChannel textChannel) {
                textChannel.sendMessage(message).queue();
            } else {
                getLogger().error(getId() + " cant send message to non TextChannel");
            }
        } else {
            getLogger().error(getId() + " cant reply or send message");
        }
    }

    public String getMessage() {
        return message;
    }
}
