package ua.mani123.discord.action;

import java.util.HashSet;
import java.util.Set;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.Channel;

public class TempData {

  Set<UserSnowflake> userSnowflakes = new HashSet<>();
  Set<Channel> channels = new HashSet<>();

  public Set<UserSnowflake> getUserSnowflakes() {
    return userSnowflakes;
  }

  public Set<Channel> getChannels() {
    return channels;
  }


}
