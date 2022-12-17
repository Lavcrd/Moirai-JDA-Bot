package vitals.handler.contexts.games;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import vitals.handler.Contexts;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

import static data.storage.users.UserSetRetrieve.getUserSet;

public class ThrowCoins implements Contexts {
    @Override
    public void execute(UserContextInteractionEvent event, GuildSet guildSet) {
        UserSet actingUserSet = getUserSet(guildSet, Objects.requireNonNull(event.getUser()));
        UserSet targetUserSet = getUserSet(guildSet, Objects.requireNonNull(event.getTarget()));
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLACK);

        if (actingUserSet.getUserId().equals(targetUserSet.getUserId())) {
            eb.setDescription("You threw coins at yourself.\nThere is a certain job for you.");
            event.deferReply().addEmbeds(eb.build()).setEphemeral(true).queue();
            return;
        }

        if (actingUserSet.getBalance() <= 1) {
            eb.setDescription("No coins to spare...");
            event.deferReply().addEmbeds(eb.build()).setEphemeral(true).queue();
            return;
        }

        Random rd = new Random();
        if (actingUserSet.getBalance() <= 40) {
            int value = rd.nextInt((actingUserSet.getBalance() / 2));

            eb.setDescription("You threw ")
                    .appendDescription(value + " coins at ")
                    .appendDescription(targetUserSet.getName())
                    .appendDescription("!");

            actingUserSet.addBalance(-value, false);
            targetUserSet.addBalance(value, false);

            event.deferReply().addEmbeds(eb.build()).setEphemeral(true).queue();
            return;
        }

        if (actingUserSet.getBalance() > 40) {
            int value = rd.nextInt(40);

            eb.setDescription("You threw ")
                    .appendDescription(value + " coins at ")
                    .appendDescription(targetUserSet.getName())
                    .appendDescription("!");

            actingUserSet.addBalance(-value, false);
            targetUserSet.addBalance(value, false);

            event.deferReply().addEmbeds(eb.build()).setEphemeral(true).queue();
        }
    }

    @Override
    public String getName() {
        return "2. Throw coins";
    }
}
