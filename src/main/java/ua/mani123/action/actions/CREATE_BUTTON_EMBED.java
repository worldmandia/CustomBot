package ua.mani123.action.actions;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import ua.mani123.action.Action;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.interactions.BUTTON_INTERACTION;
import ua.mani123.utils.ReplyReason;

import java.util.ArrayList;
import java.util.Collection;

public class CREATE_BUTTON_EMBED implements Action {

    String id;
    String embedTitle;
    String embedDescription;
    String embedColor;
    Collection<Interaction> buttons;
    ReplyReason replyReason;


    public CREATE_BUTTON_EMBED(String id, String embedTitle, String embedDescription, String embedColor, Collection<Interaction> buttons, ReplyReason replyReason) {
        this.id = id;
        this.embedTitle = embedTitle;
        this.embedDescription = embedDescription;
        this.embedColor = embedColor;
        this.buttons = buttons;
        this.replyReason = replyReason;
    }

    public ReplyReason getReplyReason() {
        return replyReason;
    }

    public String getEmbedTitle() {
        return embedTitle;
    }

    public String getEmbedDescription() {
        return embedDescription;
    }

    public String getEmbedColor() {
        return embedColor;
    }

    public ArrayList<Button> getButtons() {
        ArrayList<Button> completeButtons = new ArrayList<>();
        for (Interaction btn: buttons) {
            if (btn instanceof BUTTON_INTERACTION b){
                completeButtons.add(Button.of(ButtonStyle.valueOf(b.getButtonStyle().toUpperCase()), b.getId(), b.getButtonText()));
            }
        }
        return completeButtons;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isOnlyCommand() {
        return false;
    }
}
