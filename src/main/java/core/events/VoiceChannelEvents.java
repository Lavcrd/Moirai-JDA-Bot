package core.events;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import vitals.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

import static data.configuration.Configuration.*;

import static data.storage.guilds.GuildSetRetrieve.getGuildSet;

public class VoiceChannelEvents extends ListenerAdapter {
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        GuildSet guildSet = getGuildSet(event.getGuild().getId());
        UserSet userSet = UserSetRetrieve.getUserSet(guildSet, event.getMember().getUser());
        userSet.addBalance(1, true);

        if (event.getChannelJoined().getMembers().toArray().length > 1) return;

        //---------------------------------------------------------------------//
        User user = event.getMember().getUser();

        if (isCreateVoiceChannel(event.getChannelJoined().getId(), guildSet)) {
            event.getChannelJoined().getPermissionContainer();
            VoiceChannel channel = (VoiceChannel) event.getVoiceState().getChannel();

            assert channel != null;
            channel.createCopy(event.getGuild()).setName(user.getName() + guildSet.getChannelSuffix()).complete();

            VoiceChannel channelCreated = event.getGuild()
                    .getVoiceChannelsByName(user.getName() + guildSet.getChannelSuffix(), false)
                    .get(event.getGuild().getVoiceChannelsByName(user.getName() + guildSet.getChannelSuffix(), false).size() - 1);
            try {
                event.getGuild().moveVoiceMember(event.getMember(), channelCreated).queue();
            } catch (NullPointerException e) {
                if (channelCreated.getMembers().toArray().length < 1) {
                    channelCreated.delete().queue();
                }
            }
        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        GuildSet guildSet = getGuildSet(event.getGuild().getId());
        UserSet userSet = UserSetRetrieve.getUserSet(guildSet, event.getMember().getUser());
        userSet.addBalance(1, true);

        if (isCreateVoiceChannel(event.getChannelJoined().getId(), guildSet)) {
            if (event.getChannelJoined().getMembers().toArray().length > 1)
                return;

            //---------------------------------------------------------------------//
            User user = event.getMember().getUser();

            VoiceChannel channel = (VoiceChannel) event.getVoiceState().getChannel();
            assert channel != null;
            channel.createCopy(event.getGuild()).setName(user.getName() + guildSet.getChannelSuffix()).complete();

            VoiceChannel channelCreated = event.getGuild()
                    .getVoiceChannelsByName(user.getName() + guildSet.getChannelSuffix(), false)
                    .get(event.getGuild().getVoiceChannelsByName(user.getName() + guildSet.getChannelSuffix(), false).size() - 1);
            try {
                event.getGuild().moveVoiceMember(event.getMember(), channelCreated).queue();
            } catch (NullPointerException e) {
                if (channelCreated.getMembers().toArray().length < 1) {
                    channelCreated.delete().queue();
                }
            }
        }

        //below leave mechanic
        if (event.getChannelLeft().getMembers().toArray().length == 1 && event.getChannelLeft().getMembers().get(0).getUser().getId().equals(getBotAccountID())) {
            PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.destroy();
            event.getGuild().getAudioManager().closeAudioConnection();
        }

        if (event.getChannelLeft().getMembers().isEmpty() && event.getChannelLeft().getName().endsWith(guildSet.getChannelSuffix())) {
            event.getChannelLeft().delete().queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        GuildSet guildSet = getGuildSet(event.getGuild().getId());
        UserSet userSet = UserSetRetrieve.getUserSet(guildSet, event.getMember().getUser());
        userSet.addBalance(1, true);

        if (event.getChannelLeft().getMembers().toArray().length == 1 && event.getChannelLeft().getMembers().get(0).getUser().getId().equals(getBotAccountID())) {
            PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.destroy();
            event.getGuild().getAudioManager().closeAudioConnection();
        }

        if (event.getChannelLeft().getMembers().isEmpty() && event.getChannelLeft().getName().endsWith(guildSet.getChannelSuffix())) {
            event.getChannelLeft().delete().queue();
        }
    }

    private static boolean isCreateVoiceChannel(String channelId, GuildSet guildSet) {
        if(guildSet.getCreateChannelIds().isEmpty()) {
            return false;
        }
        return guildSet.getCreateChannelIds().contains(channelId);
    }
}
