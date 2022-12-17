package data.storage.guilds;

import static core.HeartOfTheBot.guilds;

public class GuildSetRetrieve {
    public static GuildSet getGuildSet(String guildId) {
        for (GuildSet guild:guilds) {
            if(guild.getGuildId().equals(guildId)) {
                return guild;
            }
        }
        GuildSet newGuild = new GuildSet(guildId);
        guilds.add(newGuild);
        return newGuild;
    }
}
