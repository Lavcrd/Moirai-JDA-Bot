package core;

import data.configuration.Configuration;
import data.storage.guilds.GuildSet;
import vitals.handler.Commands;
import core.events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import vitals.handler.Contexts;
import vitals.handler.commands.games.*;
import vitals.handler.commands.manage.*;
import vitals.handler.commands.music.*;
import vitals.handler.commands.other.*;
import vitals.handler.contexts.games.*;

import javax.security.auth.login.LoginException;
import java.util.*;

import static data.storage.guilds.GuildSetRepository.*;
import static data.storage.timers.TimerSet.checkTimers;


public class HeartOfTheBot {
    public static List<Commands> commands;
    public static List<Contexts> contexts;
    public static List<GuildSet> guilds;

    public static void main(String[] args) throws LoginException, InterruptedException {
        initiateCommands();
        initiateGuilds();

        JDA jda = JDABuilder.createDefault(Configuration.getToken())
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(Activity.watching("offtopic"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(new CommandEvents())
                .addEventListeners(new ContextEvents())
                .addEventListeners(new VoiceChannelEvents())
                .addEventListeners(new BinCleaner())
                .build().awaitReady();

        updateCommands(jda);
        updateTimerStart(jda);
    }

    private static void initiateCommands() {
        List<Commands> commandList = new LinkedList<>();
        commandList.add(new Help());
        commandList.add(new VoiceChannelCreator());
        commandList.add(new ChannelSuffix());
        commandList.add(new Prefix());
        commandList.add(new ReminderHelp());
        commandList.add(new Play());
        commandList.add(new Song());
        commandList.add(new Leave());
        commandList.add(new Stop());
        commandList.add(new Skip());
        commandList.add(new PrefixShow());
        commandList.add(new Invite());
        commandList.add(new RussianRoulette());
        commandList.add(new Leaderboards());
        commandList.add(new ReminderManage());
        commandList.add(new ManualSave());
        commandList.add(new ManualTimerCheck());

        System.out.println("Commands Initiated!");

        commands = commandList;
    }

    private static void initiateGuilds() {
            guilds = readGuilds();
    }

    private static void updateCommands(JDA jda) {
        List<Guild> guildList = jda.getGuilds();
        for (Guild guild : guildList) {
            guild.updateCommands().addCommands(
                    net.dv8tion.jda.api.interactions.commands.build.Commands.context(Command.Type.USER, "1. Status"),
                    net.dv8tion.jda.api.interactions.commands.build.Commands.context(Command.Type.USER, "2. Throw coins"),
                    net.dv8tion.jda.api.interactions.commands.build.Commands.context(Command.Type.USER, "3. Steal coins")
            ).queue();
        }

        List<Contexts> contextsList = new LinkedList<>();
        contextsList.add(new Status());
        contextsList.add(new ThrowCoins());
        contextsList.add(new Theft());

        contexts = contextsList;

        System.out.println("Context updated!");
    }

    private static void updateTimerStart(JDA jda){
        Timer timer = new Timer();
        class SaveTask extends TimerTask {
            @Override
            public void run() {
                saveGuilds(guilds);
                checkTimers(jda);
            }
        }

        guilds.stream().flatMap(guilds -> guilds.getTimersList().stream())
                        .forEach(timerSet -> timerSet.setActive(false));

        timer.schedule(new SaveTask(), 60*60*1000, 60*60*1000);
    }
}
