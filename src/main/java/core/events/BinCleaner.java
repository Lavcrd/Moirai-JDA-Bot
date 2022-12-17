package core.events;

import data.storage.guilds.GuildSet;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static core.HeartOfTheBot.guilds;
import static data.storage.guilds.GuildSetRetrieve.getGuildSet;

public class BinCleaner extends ListenerAdapter {
    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        List<String> guildIdListJDA = event.getJDA().getGuilds().stream().map(ISnowflake::getId).collect(Collectors.toList());
        if (guildIdListJDA.isEmpty()) return;

        if (guilds.stream().map(GuildSet::getGuildId).collect(Collectors.toList()).contains(event.getGuild().getId())) {
            guilds.remove(getGuildSet(event.getGuild().getId()));
        }


        List<GuildSet> substituteList;
        substituteList = guilds;

        //Additional clean in case of offline kick
        for (GuildSet guildSet : substituteList) {
            if (!guildIdListJDA.contains(guildSet.getGuildId())) {
                guilds.remove(guildSet);
            }
        }
    }
}
