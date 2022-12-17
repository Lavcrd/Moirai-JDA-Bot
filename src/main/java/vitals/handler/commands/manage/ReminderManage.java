package vitals.handler.commands.manage;

import data.storage.guilds.GuildSet;
import data.storage.timers.TimerSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vitals.handler.Commands;

import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.stream.Collectors;

import static vitals.helper.Helper.deletePrefix;
import static vitals.helper.Helper.isImportant;

public class ReminderManage implements Commands {
    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        final byte TIMER_SIZE_LIMIT = 5;

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLACK);

        if (!isImportant(event)) {
            event.getChannel().sendMessageEmbeds(eb.setDescription("Sorry " + event.getAuthor().getAsMention() + ", you have not met requirements for this command!").build()).queue();
            return;
        }

        String command = deletePrefix(event.getMessage().getContentRaw(), guildSet);
        String[] elements;
        elements = command.trim().split("\\s+");

        if (elements[1].equals("add") && elements.length >= 7) {
            if(guildSet.getTimersList().size() >= TIMER_SIZE_LIMIT) {
                eb.setDescription("You have reached this discord reminders limit!\nCheck ")
                        .appendDescription(guildSet.getPrefix())
                        .appendDescription("r help to look up your timers!");
                event.getMessage().replyEmbeds(eb.build()).queue();
                return;
            }

            if(!event.getGuild().getTextChannels().stream().map(ISnowflake::getId).collect(Collectors.toList()).contains(String.valueOf(elements[5]))) {
                eb.setDescription("No channel found!");
                event.getMessage().replyEmbeds(eb.build()).queue();
                return;
            }
            try {
                LocalTime.of(Byte.parseByte(elements[3]), Byte.parseByte(elements[4]));
            } catch (DateTimeException e) {
                eb.setDescription("Incorrect time values!");
                event.getMessage().replyEmbeds(eb.build()).queue();
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 6; i < elements.length; i++) {
                sb.append(elements[i]).append(" ");
            }

            try {
                guildSet.getTimersList().add(new TimerSet(guildSet.getGuildId(), elements[2], Byte.parseByte(elements[3]), Byte.parseByte(elements[4]), elements[5], sb.toString()));
            } catch (Exception e) {
                eb.setDescription("Something went wrong :(");
                event.getMessage().replyEmbeds(eb.build()).queue();
            }

        } else if (elements[1].equals("remove") && elements.length == 3) {

            if(guildSet.getTimersList().isEmpty()) {
                eb.setDescription("There are no reminders!");
                event.getMessage().replyEmbeds(eb.build()).queue();
                return;
            } else if (!guildSet.getTimersList().stream().map(TimerSet::getTitle).collect(Collectors.toList()).contains(elements[2])) {
                eb.setDescription(elements[2]).appendDescription(" was not found in reminder list!");
                event.getMessage().replyEmbeds(eb.build()).queue();
                return;
            } else {
                guildSet.getTimersList().removeIf(timerSet -> timerSet.getTitle().equals(elements[2]));
            }

        } else {
            eb.setDescription("Incorrect syntax!");
            event.getMessage().replyEmbeds(eb.build()).queue();
            return;
        }
        eb.setDescription("Success!");
        event.getMessage().replyEmbeds(eb.build()).queue();
        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);
    }

    @Override
    public String getName() {
        return "reminder";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return "";
    }
}
