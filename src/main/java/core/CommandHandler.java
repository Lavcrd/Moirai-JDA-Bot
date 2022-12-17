package core;

import data.storage.guilds.GuildSet;
import vitals.handler.Commands;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static vitals.helper.Helper.deletePrefix;

public class CommandHandler {
    public static void CommandExecute(String messageSent, MessageReceivedEvent event, GuildSet guildSet) {
        String receivedCommand = deletePrefix(messageSent, guildSet);

        if (receivedCommand.isEmpty()) {
            return;
        }

        for (Commands command : HeartOfTheBot.commands) {
            if (command == null) break;
            if (receivedCommand.startsWith(command.getName())) {
                command.execute(event, guildSet);
                return;
            }
        }
        event.getMessage().reply("> Hey " + event.getAuthor().getAsMention() + ", this command is incorrect. Try again!").queue();
    }
}
