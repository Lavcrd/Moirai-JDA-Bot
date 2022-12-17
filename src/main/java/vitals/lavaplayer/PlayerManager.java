package vitals.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.BLACK).setTitle("Added: ").setDescription("Title: ")
                        .appendDescription(audioTrack.getInfo().title)
                        .appendDescription("\n Author: ")
                        .appendDescription(audioTrack.getInfo().author);

                textChannel.sendMessageEmbeds(eb.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                List<AudioTrack> tracks = audioPlaylist.getTracks();

                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.BLACK).setTitle("Added playlist: ")
                        .setDescription(audioPlaylist.getName())
                        .appendDescription("\n Size (")
                        .appendDescription(String.valueOf(tracks.size()))
                        .appendDescription(")");

                textChannel.sendMessageEmbeds(eb.build()).queue();
                AudioTrack selected = audioPlaylist.getSelectedTrack();

                musicManager.scheduler.queue(selected);

                byte size = (byte)Math.min(audioPlaylist.getTracks().size(), 100);
                for (byte i = 0; i < size; i++) {
                    if (tracks.get(i) != selected) {
                        musicManager.scheduler.queue(tracks.get(i));
                    }
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.BLACK).setTitle("Enqueue fail!").setDescription("Failed to find anything with: " + trackURL);
                textChannel.sendMessageEmbeds(eb.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.BLACK).setTitle("Link fail!").setDescription("Failed to play: " + trackURL);
                textChannel.sendMessageEmbeds(eb.build()).queue();
            }
        });
    }

    public static PlayerManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
