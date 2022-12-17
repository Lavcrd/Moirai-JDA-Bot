package vitals.handler.commands.music;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import vitals.handler.Commands;
import vitals.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class Leave implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        final VoiceChannel connectedChannel = (VoiceChannel) event.getGuild().getSelfMember().getVoiceState().getChannel();
        if (connectedChannel == null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setDescription("I'm not in the voice channel :c");
            event.getTextChannel().sendMessageEmbeds(eb.build()).queue();
        } else {
            PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.audioPlayer.destroy();
            event.getGuild().getAudioManager().closeAudioConnection();
        }

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(3, true);
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return "└ " + guildSet.getPrefix() + "leave\t—\tBot leaves channel.\n";
    }
}
