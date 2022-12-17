package vitals.handler.commands.other;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import vitals.handler.Commands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

import static data.configuration.Configuration.*;

public class PrefixShow implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        StringBuilder sb = new StringBuilder();
        EmbedBuilder eb = new EmbedBuilder();

        sb.append("Currently my prefix is \"")
                .append(guildSet.getPrefix())
                .append("\".\n")
                .append("Try \"")
                .append(guildSet.getPrefix())
                .append("help\" in command text channel ;))");

        eb.setColor(Color.BLACK).setTitle("Current prefix!").setDescription(sb);

        event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(eb.build()).queue());
        event.getMessage().delete().queue();

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);
    }

    @Override
    public String getName() {
        return getBotMention();
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return getName() + "\t—\tReplies with current prefix. (Might be of use)\n";
    }

}
