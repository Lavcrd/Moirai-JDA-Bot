package vitals.handler.commands.music;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import vitals.handler.Commands;
import vitals.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.*;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

import static vitals.helper.Helper.deletePrefix;

public class Play implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        String command = deletePrefix(event.getMessage().getContentRaw(), guildSet);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLACK);

        final VoiceChannel connectedChannel = (VoiceChannel) event.getGuild().getSelfMember().getVoiceState().getChannel();
        final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

        if (memberChannel != connectedChannel && connectedChannel != null) {
            event.getMessage().getChannel().sendMessageEmbeds(eb.setDescription("You need to be together with bot on voice channel!").build()).queue();
            return;
        } else if (memberChannel == null) {
            event.getMessage().getChannel().sendMessageEmbeds(eb.setDescription("You need to be on voice channel to run this command!").build()).queue();
            return;
        }

        String link = command.substring(5).trim();

        if (!isUrl(link)) {
            event.getMessage().replyEmbeds(eb.setDescription("Invalid link!").build()).queue();
            return;
        }

        if (connectedChannel == null) {
            final AudioManager audioManager = event.getGuild().getAudioManager();
            audioManager.openAudioConnection(memberChannel);
            audioManager.getGuild().getAudioManager().setSelfDeafened(true);
        }

        PlayerManager.getINSTANCE().loadAndPlay(event.getMessage().getChannel().asTextChannel(), link);

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(4, true);
    }

    public static boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return guildSet.getPrefix() + "play linkhere\t—\tAdds music track to queue.\n";
    }
}



