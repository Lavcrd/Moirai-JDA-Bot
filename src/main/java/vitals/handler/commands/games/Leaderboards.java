package vitals.handler.commands.games;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vitals.handler.Commands;

import java.awt.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static core.HeartOfTheBot.guilds;

public class Leaderboards implements Commands {
    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLACK).setTitle("**Highscores**");

        StringBuilder sb1 = new StringBuilder();
        byte i = 0;
        for (UserSet guildUser :
                guildSet.getUserSets().stream()
                        .sorted((u1, u2) -> u2.getBalance() - u1.getBalance())
                        .collect(Collectors.toList())) {
            if (i == 15) break;
            sb1.append(i + 1).append(".\t")
                    .append(guildUser.getName()).append("#").append(guildUser.getDiscriminator())
                    .append("\t—\t")
                    .append(guildUser.getBalance()).append(" coins\n");
            i++;
        }
        eb.addField("**Local:** ", sb1.toString(), true);

        StringBuilder sb2 = new StringBuilder();
        byte j = 0;
        for (UserSet allGuildUser :
                guilds.stream().flatMap(guildSet1 -> guildSet1.getUserSets().stream())
                        .sorted((u1, u2) -> u2.getBalance() - u1.getBalance())
                        .filter(distinctByKey(UserSet::getUserId))
                        .collect(Collectors.toList())) {
            if (j == 15) break;
            sb2.append(j + 1).append(".\t")
                    .append(allGuildUser.getName()).append("#").append(allGuildUser.getDiscriminator())
                    .append("\t—\t")
                    .append(allGuildUser.getBalance()).append(" coins\n");
            j++;
        }
        eb.addField("**Global:** ", sb2.toString(), true);

        eb.setDescription("**Your score:** \n")
                .appendDescription(user.getName()).appendDescription("#").appendDescription(user.getDiscriminator())
                .appendDescription("\t—\t")
                .appendDescription(user.getBalance() + "").appendDescription(" coins\n");

        event.getMessage().replyEmbeds(eb.build()).queue();
    }

    @Override
    public String getName() {
        return "highscores";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return guildSet.getPrefix() + "highscores\t—\tSends current highscores.\n";
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
