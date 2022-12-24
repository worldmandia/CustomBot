package ua.mani123.discord.action;

import java.util.HashSet;
import java.util.Set;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class TempData {

  HashSet<UserSnowflake> userSnowflakes = new HashSet<>();
  HashSet<VoiceChannel> voiceChannels = new HashSet<>();
  HashSet<TextChannel> textChannels = new HashSet<>();
  HashSet<Role> roles = new HashSet<>();

  public Set<UserSnowflake> getUserSnowflakes() {
    return userSnowflakes;
  }

  public Set<VoiceChannel> getVoiceChannels() {
    return voiceChannels;
  }

  public Set<TextChannel> getTextChannels() {
    return textChannels;
  }

  public Set<Role> getRoles() {
    return roles;
  }
}
