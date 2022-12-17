package vitals.handler.commands.games;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vitals.handler.Commands;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static vitals.helper.Helper.isImportant;

public class RussianRoulette implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        if(event.getGuild().getTextChannels().size() == 0) return;
        if(isImportant(event)) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setDescription("You can't play this game :v. (Admin role)");
            event.getMessage().replyEmbeds(eb.build()).queue();
            return;
        }

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        if(user.getBalance() < 50) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setDescription("You have insufficient currency to play this game! (min. 50)");
            event.getMessage().replyEmbeds(eb.build()).queue();
            return;
        }

        Random rd = new Random();

        if(rd.nextInt(6) == 3 ) {
            user.addBalance(-50, false);
            event.getGuild().getTextChannels().get(0).createInvite().setMaxUses(1).setMaxAge(3L, TimeUnit.DAYS).complete();
            Invite invite = event.getGuild().retrieveInvites().complete().get(0);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setDescription("You died!");

            event.getMember().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(eb.build()).queue());
            event.getMember().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(invite.getUrl()).queue());
        } else {
            user.addBalance(25, false);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setDescription("You were lucky this time.");
            event.getMessage().replyEmbeds(eb.build()).queue();
            return;
        }
        event.getMember().kick("Died!").queueAfter(1L,TimeUnit.SECONDS);
    }

    @Override
    public String getName() {
        return "russian";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return "";
    }
}
