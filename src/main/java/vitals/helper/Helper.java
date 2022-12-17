package vitals.helper;

import data.storage.guilds.GuildSet;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

import static data.configuration.Configuration.*;

public class Helper {
    //User distinction
    public static boolean isImportant(MessageReceivedEvent event) {
        return Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || event.getAuthor().getId().equals(getBotOwnerID());
    }

    //Command Helper
    public static String deletePrefix(String messageSent, GuildSet guildSet) {
        if (messageSent.trim().startsWith("<@")) return messageSent;
        return messageSent.trim().substring(guildSet.getPrefix().length());
    }
}
