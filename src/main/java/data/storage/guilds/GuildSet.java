package data.storage.guilds;

import data.storage.timers.TimerSet;
import data.storage.users.UserSet;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class GuildSet implements Serializable {
    private final static long serialVersionUID = -445051942453004299L;
    private final String guildId;
    private String prefix;
    private String channelSuffix;
    private final List<String> createChannelIds;
    private final List<UserSet> userSets;
    private final List<TimerSet> timersList;



    public GuildSet(String guildId) {
        this.guildId = guildId;
        this.prefix = "pls ";
        this.channelSuffix = "'s Channel";
        this.createChannelIds = new ArrayList<>();
        this.userSets = new LinkedList<>();
        this.timersList = new LinkedList<>();
    }


    //Getters
    public String getGuildId() {
        return guildId;
    }
    public String getPrefix() {
        return prefix;
    }
    public String getChannelSuffix() {
        return channelSuffix;
    }
    public List<String> getCreateChannelIds() {
        return createChannelIds;
    }
    public byte getCreateChannelSize() {
        return (byte)this.createChannelIds.size();
    }
    public List<UserSet> getUserSets() {
        return userSets;
    }
    public List<TimerSet> getTimersList() {
        return timersList;
    }



    //Setters
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public void setChannelSuffix(String channelSuffix) {
        this.channelSuffix = channelSuffix;
    }
    public void addCreateChannelId(String createChannelIds) {
        this.createChannelIds.add(createChannelIds);
    }


    //Methods
    public void fixCreateChannelSize(MessageReceivedEvent event) {
        if (this.createChannelIds.isEmpty()) return;

        List<String> guildChannelIds = event.getGuild()
                .getVoiceChannels()
                .stream()
                .map(ISnowflake::getId)
                .collect(Collectors.toList());

        for (byte i = 0; i < this.createChannelIds.size(); i++) {
            if(!guildChannelIds.contains(this.createChannelIds.get(i))) this.createChannelIds.remove(this.createChannelIds.get(i));
        }
    }
}
