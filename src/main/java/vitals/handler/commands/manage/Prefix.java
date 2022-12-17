package vitals.handler.commands.manage;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import vitals.handler.Commands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;


import static vitals.helper.Helper.*;

public class Prefix implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLACK);

        if (!isImportant(event)) {
            event.getChannel().sendMessageEmbeds(eb.setDescription("Sorry " + event.getAuthor().getAsMention() + ", you have not met requirements for this command!").build()).queue();
            return;
        }

        String messageSent = deletePrefix(event.getMessage().getContentRaw(), guildSet);

        String newPrefix = messageSent.substring(this.getName().length()).trim().replaceAll("\"", "").replaceAll("^\\s*", "");
        if (newPrefix.isEmpty()) {
            event.getChannel().sendMessageEmbeds(eb.setDescription("Sorry " + event.getAuthor().getAsMention() + ", this prefix is not allowed!").build()).queue();
            return;
        }
        guildSet.setPrefix(newPrefix);
        event.getMessage().replyEmbeds(eb.setDescription("My new prefix is \"" + newPrefix + "\".").build()).queue();

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return guildSet.getPrefix() + "prefix \"PrefixHere\"\t—\tChanges current prefix to new one. (Only Admins)\n";
    }
}