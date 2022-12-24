package ua.mani123.discord.action;

import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Set;
import net.dv8tion.jda.api.entities.channel.Channel;

public class TempData {

  Set<Member> members = new HashSet<>();
  Set<Channel> channels = new HashSet<>();

  public Set<Member> getMembers() {
    return members;
  }

  public Set<Channel> getChannels() {
    return channels;
  }
}
