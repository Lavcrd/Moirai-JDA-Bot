package vitals.handler.commands.music;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import vitals.handler.Commands;
import vitals.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class Skip implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.nextTrack();

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(2, true);
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return "└ " + guildSet.getPrefix() + "skip\t—\tSkips current track.\n";
    }
}
