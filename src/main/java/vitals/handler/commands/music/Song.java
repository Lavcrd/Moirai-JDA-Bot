package vitals.handler.commands.music;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import vitals.handler.Commands;
import vitals.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;


public class Song implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        if (PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack() == null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setTitle("Current track: ").setDescription("Currently there are no tracks!");
            event.getChannel().sendMessageEmbeds(eb.build()).queue();
            return;
        }

        String title = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack().getInfo().title;
        String author = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack().getInfo().author;
        String link = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack().getInfo().uri;

        long duration = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack().getInfo().length;
        String decimalsDuration = String.valueOf(Math.floor(duration%60000)).substring(0,2);
        String durationFix = Math.floorDiv(duration, 60000) + ":" + decimalsDuration;

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLACK).setTitle("Current track: ")
                .setDescription("Title: ")
                .appendDescription(title)
                .appendDescription("\n Author: ")
                .appendDescription(author)
                .appendDescription("\nLink: ")
                .appendDescription(link)
                .appendDescription("\nLength: ")
                .appendDescription(durationFix);

        event.getTextChannel().sendMessageEmbeds(eb.build()).queue();

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(2, true);
    }

    @Override
    public String getName() {
        return "song";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return "└ " + guildSet.getPrefix() + "song\t—\tSends message with current song info.\n";
    }
}
