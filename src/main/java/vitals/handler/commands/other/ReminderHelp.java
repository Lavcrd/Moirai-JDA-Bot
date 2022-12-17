package vitals.handler.commands.other;

import data.storage.guilds.GuildSet;
import data.storage.timers.TimerSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vitals.handler.Commands;

import java.awt.*;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import static vitals.helper.Helper.isImportant;

public class ReminderHelp implements Commands {
    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        EmbedBuilder eb = new EmbedBuilder();
        event.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);

        if (!isImportant(event)) {
            event.getChannel().sendMessageEmbeds(eb.setDescription("Sorry " + event.getAuthor().getAsMention() + ", you have not met requirements for this command!").build()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();

        if (guildSet.getTimersList().isEmpty()) {
            sb.append("— Empty —");
        } else {
            for (TimerSet timerSet : guildSet.getTimersList()) {
                sb.append(timerSet.getTitle()).append("\t").append(LocalTime.of(timerSet.getHour(), timerSet.getMinute())).append("\n");
            }
        }

        eb.setColor(Color.BLACK).setTitle(event.getGuild().getName())
                .setDescription("**To add new announcements:**\n")
                .appendDescription("**Syntax:** ")
                .appendDescription(guildSet.getPrefix()).appendDescription("reminder add A B C D E\n")
                .appendDescription("**Where:** \nA = title (One word)\nB = hour\nC = minute\nD = textchannel ID\nE = message\n")
                .appendDescription("—\t—\t—\t—\t—\t—\t—\t—\t—\t—\n")
                .appendDescription("**To remove old announcements:**\n")
                .appendDescription("**Syntax:** ")
                .appendDescription(guildSet.getPrefix()).appendDescription("reminder remove A\n")
                .appendDescription("**Where:** \nA = title\n")
                .appendDescription("—\t—\t—\t—\t—\t—\t—\t—\t—\t—\n")
                .appendDescription("Updates might take up to 1 hour.\nThere is limit of 5 reminders\n")
                .addField("**Current reminders:** ", sb.toString(), true);

        event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(eb.build()).queue());
        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);
    }

    @Override
    public String getName() {
        return "r help";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return guildSet.getPrefix() + "r help\t—\tAnnouncement setup instructions. (Only Admins)\n";
    }
}
