package ua.mani123.discordModule;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;

import java.util.ArrayList;

@Getter
public class TempData {

    private final static ArrayList<Role> roles = new ArrayList<>();
    private final static ArrayList<UserSnowflake> users = new ArrayList<>();

    public void clearAll() {
        roles.clear();
        users.clear();
    }
}
