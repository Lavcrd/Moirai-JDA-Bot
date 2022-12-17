package vitals.handler.commands.music;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import vitals.handler.Commands;
import vitals.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class Stop implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.stopTrack();

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(3, true);
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return "└ " + guildSet.getPrefix() + "stop\t—\tClears whole playlist.\n";
    }
}
