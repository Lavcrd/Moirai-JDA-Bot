package vitals.handler.commands.other;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import vitals.handler.Commands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

import static data.configuration.Configuration.getBotInviteLink;

public class Invite implements Commands {
    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        EmbedBuilder eb = new EmbedBuilder();
        Button button = Button.link(getBotInviteLink(), "Click!");

        eb.setColor(Color.BLACK).setTitle("Invite me!");
        event.getMessage().getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(eb.build()).setActionRow(button).queue());

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return guildSet.getPrefix() + "invite\t—\tSends 'bot invite' with DM!\n";
    }
}
