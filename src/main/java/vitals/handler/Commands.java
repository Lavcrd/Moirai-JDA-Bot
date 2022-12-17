package vitals.handler;

import data.storage.guilds.GuildSet;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Commands {
    void execute(MessageReceivedEvent event, GuildSet guildSet);
    String getName();
    String getDescription(GuildSet guildSet);
}
