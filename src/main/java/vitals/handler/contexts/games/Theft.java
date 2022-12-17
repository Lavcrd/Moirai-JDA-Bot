package vitals.handler.contexts.games;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import vitals.handler.Contexts;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

import static data.storage.users.UserSetRetrieve.getUserSet;

public class Theft implements Contexts {
    @Override
    public void execute(UserContextInteractionEvent event, GuildSet guildSet) {
        UserSet actingUserSet = getUserSet(guildSet, Objects.requireNonNull(event.getUser()));
        UserSet targetUserSet = getUserSet(guildSet, Objects.requireNonNull(event.getTarget()));

        if(actingUserSet.getUserId().equals(targetUserSet.getUserId())) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setDescription("Stealing from yourself is just like being a cuck.\nNice try, kleptomaniac.");
            event.deferReply().addEmbeds(eb.build()).setEphemeral(true).queue();
            return;
        }

        if (targetUserSet.getStealProtection().isAfter(LocalDateTime.now())) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setDescription("This person was recently robbed.\nHe's a little bit too wary of his surroundings.");
            event.deferReply().addEmbeds(eb.build()).setEphemeral(true).queue();
            return;
        } else if (targetUserSet.getBalance() < 10) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setDescription("This person has empty pockets.");
            event.deferReply().addEmbeds(eb.build()).setEphemeral(true).queue();
            targetUserSet.setStealProtection();
            return;
        }

        targetUserSet.setStealProtection();
        Random rd = new Random();

        float percentage = rd.nextInt(300) * 0.001f;
        EmbedBuilder eb = new EmbedBuilder();
        int value = Math.round(targetUserSet.getBalance()*percentage);

        eb.setColor(Color.BLACK).setDescription("You stole ").appendDescription(value + " coins!");

        actingUserSet.addBalance(value, false);
        targetUserSet.addBalance(-value, false);

        event.deferReply().addEmbeds(eb.build()).setEphemeral(true).queue();
    }

    @Override
    public String getName() {
        return "4. Try to steal coins";
    }
}
