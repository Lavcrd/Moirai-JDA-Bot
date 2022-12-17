package data.storage.timers;

import data.storage.guilds.GuildSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import java.awt.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static core.HeartOfTheBot.guilds;
import static data.storage.guilds.GuildSetRetrieve.getGuildSet;

public class TimerSet implements Serializable {

    private final static long serialVersionUID = 3371706321253229763L;
    private final String parentGuildId;
    private final String title;
    private final byte hour;
    private final byte minute;
    private final String channelId;
    private final String message;
    private boolean isActive;

    public TimerSet(String parentGuildId, String title, byte hour, byte minute, String channelId, String message) {
        this.parentGuildId = parentGuildId;
        this.title = title;
        this.hour = hour;
        this.minute = minute;
        this.channelId = channelId;
        this.message = message;
        this.isActive = false;
    }

    //Getters
    public String getTitle() {
        return title;
    }
    public byte getHour() {
        return hour;
    }
    public byte getMinute() {return minute;}

    //Setters
    public void setActive(boolean active) {
        isActive = active;
    }


    //Methods
    public static void checkTimers(JDA jda) {
        List<TimerSet> timerSets = guilds.stream().flatMap(guildSet -> guildSet.getTimersList().stream()).collect(Collectors.toList());
        for (TimerSet timerSet : timerSets) {
            if (timerSet.isActive) continue;
            if (LocalTime.now().isAfter(LocalTime.of(timerSet.hour, timerSet.minute)) && timerSet.getHour() != 0) continue;

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.BLACK).setTitle(timerSet.title).setDescription(timerSet.message);

                    try {
                        //noinspection ConstantConditions
                        jda.getGuildChannelById(timerSet.channelId)
                                .getGuild()
                                .getTextChannelById(timerSet.channelId)
                                .sendMessageEmbeds(eb.build())
                                .queue();
                        timerSet.isActive = false;
                    } catch (Exception e) {
                        GuildSet guildSet = getGuildSet(timerSet.parentGuildId);
                        guildSet.getTimersList().remove(timerSet);
                    }
                }
            };

            Timer timer = new Timer();
            LocalTime timerSetTime = LocalTime.of(timerSet.getHour(), timerSet.getMinute(), 0);
            LocalDateTime timerSetDateTime = LocalDateTime.of(LocalDate.now(), timerSetTime);
            long time;

            if(timerSetTime.isAfter(LocalTime.now())){
                time = Duration.between(LocalDateTime.now(), timerSetDateTime).toMillis();
            } else {
                time = Duration.between(LocalDateTime.now(), timerSetDateTime.plusDays(1)).toMillis();
            }

            timer.schedule(timerTask, time);
            timerSet.isActive = true;
        }
    }
}
