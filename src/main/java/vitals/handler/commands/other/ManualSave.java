package vitals.handler.commands.other;

import core.HeartOfTheBot;
import data.configuration.Configuration;
import data.storage.guilds.GuildSet;
import data.storage.guilds.GuildSetRepository;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vitals.handler.Commands;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class ManualSave implements Commands {
    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        event.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
        if (!event.getAuthor().getId().equals(Configuration.getBotOwnerID())) return;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLACK);
        try {
            GuildSetRepository.saveGuilds(HeartOfTheBot.guilds);
            eb.setDescription("Success!");
            event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(eb.build()).queue());
        } catch (Exception e) {
            eb.setDescription("Failure!");
            event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(eb.build()).queue());
        }
        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);
    }

    @Override
    public String getName() {
        return "manualsave";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return "";
    }
}
