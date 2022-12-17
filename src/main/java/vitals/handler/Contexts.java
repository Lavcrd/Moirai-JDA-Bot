package vitals.handler;

import data.storage.guilds.GuildSet;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;

public interface Contexts {
    void execute(UserContextInteractionEvent event, GuildSet guildSet);
    String getName();
}
