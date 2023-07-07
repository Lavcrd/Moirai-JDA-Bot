package core.events;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import vitals.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static data.configuration.Configuration.*;

import static data.storage.guilds.GuildSetRetrieve.getGuildSet;

public class VoiceChannelEvents extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (event.getOldValue() == null) {
            onGuildVoiceJoin(event);
        } else if (event.getNewValue() == null) {
            onGuildVoiceLeave(event);
        } else {
            onGuildVoiceMove(event);
        }
    }

    public void onGuildVoiceJoin(GuildVoiceUpdateEvent event) {
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

    public void onGuildVoiceMove(GuildVoiceUpdateEvent event) {
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

    public void onGuildVoiceLeave(GuildVoiceUpdateEvent event) {
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
