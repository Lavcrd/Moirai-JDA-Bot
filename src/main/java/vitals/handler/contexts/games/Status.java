package vitals.handler.contexts.games;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import vitals.handler.Contexts;

import java.awt.*;
import java.time.LocalDateTime;

import static data.storage.users.UserSetRetrieve.getUserSet;

public class Status implements Contexts {
    @Override
    public void execute(UserContextInteractionEvent event, GuildSet guildSet) {
        UserSet targetUserSet = getUserSet(guildSet, event.getTarget());
        UserSet actingUserSet = getUserSet(guildSet, event.getUser());
        actingUserSet.addBalance(1, true);

        String text2 = targetUserSet.getStealProtection().toLocalTime().toString();

        if(targetUserSet.getStealProtection().isBefore(LocalDateTime.now())) {
            text2 = "\t—\t";
        }

        EmbedBuilder eb = new EmbedBuilder();
         eb.setColor(Color.BLACK).setTitle(targetUserSet.getName())
                 .setDescription("Balance:\t").appendDescription(targetUserSet.getBalance() + "\tcoins\n")
                 .appendDescription("Theft protected until: ").appendDescription(text2).appendDescription("\n");

         event.deferReply().addEmbeds(eb.build()).setEphemeral(true).queue();
    }

    @Override
    public String getName() {
        return "1. Status";
    }
}
