package core.events;

import core.CommandHandler;
import data.storage.guilds.GuildSet;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


import static data.storage.guilds.GuildSetRetrieve.getGuildSet;

public class CommandEvents extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User messageAuthor = event.getAuthor();
        if (messageAuthor.isBot()) return;
        if (!event.getChannelType().isGuild()) return;

        String messageSent = event.getMessage().getContentRaw();
        String commandChannel = event.getChannel().getName();
        GuildSet guildSet = getGuildSet(event.getGuild().getId());
        String botMention = event.getJDA().getSelfUser().getAsMention();


        if (isCommandChannel(commandChannel) || event.getMessage().getContentRaw().equals(botMention)) {
            if (messageSent.startsWith(guildSet.getPrefix()) || messageSent.equals(botMention)) {
                CommandHandler.CommandExecute(messageSent, event, guildSet);
            }
        }
    }

    private static boolean isCommandChannel(String channelName) {
        return channelName.toLowerCase().contains("kome") || channelName.toLowerCase().contains("comm") || channelName.toLowerCase().contains("muzy") || channelName.toLowerCase().contains("musi") || channelName.toLowerCase().contains("cmd");
    }
}


