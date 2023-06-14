package ua.mani123.discordModule;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class TempData {

    private final ArrayList<Role> roles = new ArrayList<>();
    private final ArrayList<UserSnowflake> users = new ArrayList<>();
    private final HashMap<String, String> strings = new HashMap<>();
    private final HashMap<String, Integer> numbers = new HashMap<>();
    private final HashMap<String, Boolean> booleans = new HashMap<>();
    private final HashMap<String, Channel> channels = new HashMap<>();

    public void clearAll() {
        roles.clear();
        users.clear();
        strings.clear();
        numbers.clear();
        booleans.clear();
        channels.clear();
    }
}
