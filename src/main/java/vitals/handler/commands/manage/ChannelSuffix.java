package vitals.handler.commands.manage;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vitals.handler.Commands;

import java.awt.*;

import static vitals.helper.Helper.deletePrefix;
import static vitals.helper.Helper.isImportant;

public class ChannelSuffix implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLACK);

        if (!isImportant(event)) {
            event.getChannel().sendMessageEmbeds(eb.setDescription("Sorry " + event.getAuthor().getAsMention() + ", you have not met requirements for this command!").build()).queue();
            return;
        }

        String messageSent = deletePrefix(event.getMessage().getContentRaw(), guildSet);

        String newSuffix = messageSent.substring(this.getName().length()).trim().replaceAll("\"", "");
        if (newSuffix.isEmpty()) {
            event.getChannel().sendMessageEmbeds(eb.setDescription("Sorry " + event.getAuthor().getAsMention() + ", this suffix is not allowed!").build()).queue();
            return;
        }
        guildSet.setChannelSuffix(newSuffix);
        event.getMessage().replyEmbeds(eb.setDescription("My new channel suffix is \"" + newSuffix + "\".\nIf there are old rooms in use, they have to be deleted manually!").build()).queue();

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);
    }

    @Override
    public String getName() {
        return "suffix";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return guildSet.getPrefix() + "suffix \"ChannelSuffix\"\t—\tChange channel suffix. (Only Admins)\n";
    }
}
