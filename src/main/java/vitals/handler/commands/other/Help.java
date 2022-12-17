package vitals.handler.commands.other;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import vitals.handler.Commands;
import core.HeartOfTheBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class Help implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        StringBuilder sb = new StringBuilder();
        EmbedBuilder eb = new EmbedBuilder();

        for (Commands command : HeartOfTheBot.commands) {
            sb.append(command.getDescription(guildSet));
        }
        eb.setColor(Color.BLACK).setTitle("**Only usable in command channels:**").setDescription(sb);

        event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(eb.build()).queue());

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return guildSet.getPrefix() + "help\t—\tReplies with this text.\n";
    }
}