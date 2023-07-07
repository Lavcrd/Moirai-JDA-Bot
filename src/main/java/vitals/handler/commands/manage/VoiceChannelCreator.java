package vitals.handler.commands.manage;

import data.storage.guilds.GuildSet;
import data.storage.users.UserSet;
import data.storage.users.UserSetRetrieve;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vitals.handler.Commands;

import java.awt.*;

public class VoiceChannelCreator implements Commands {

    @Override
    public void execute(MessageReceivedEvent event, GuildSet guildSet) {
        final byte VCHANNEL_LIMIT = 3;
        guildSet.fixCreateChannelSize(event);
        if (guildSet.getCreateChannelSize() >= VCHANNEL_LIMIT) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.BLACK).setDescription("You have reached limit of channels (" + VCHANNEL_LIMIT + ")!");
            event.getMessage().replyEmbeds(eb.build()).queue();
            return;
        }

        VoiceChannel voiceChannel = event.getGuild().createVoiceChannel("editThisChannel").complete();
        String channelId = voiceChannel.getId();
        guildSet.addCreateChannelId(channelId);

        UserSet user = UserSetRetrieve.getUserSet(guildSet, event.getAuthor());
        user.addBalance(1, true);
    }

    @Override
    public String getName() {
        return "channel";
    }

    @Override
    public String getDescription(GuildSet guildSet) {
        return guildSet.getPrefix() + "channel\t—\tCreates voice channel to create rooms. (Only Admins)\n";
    }
}
