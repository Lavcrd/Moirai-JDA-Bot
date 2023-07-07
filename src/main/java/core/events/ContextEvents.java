package core.events;

import core.HeartOfTheBot;
import data.storage.guilds.GuildSet;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import vitals.handler.Contexts;

import java.util.Objects;

import static data.storage.guilds.GuildSetRetrieve.getGuildSet;


public class ContextEvents extends ListenerAdapter {
    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
        if (Objects.requireNonNull(event.getMember()).getUser().isBot()) return;

        GuildSet guildSet = getGuildSet(Objects.requireNonNull(event.getGuild()).getId());

        for (Contexts context: HeartOfTheBot.contexts) {
            if(event.getName().equals(context.getName())) {
                context.execute(event, guildSet);
            }
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        event.getGuild().updateCommands().addCommands(
                net.dv8tion.jda.api.interactions.commands.build.Commands.context(Command.Type.USER, "1. Status"),
                net.dv8tion.jda.api.interactions.commands.build.Commands.context(Command.Type.USER, "2. Throw coins"),
                net.dv8tion.jda.api.interactions.commands.build.Commands.context(Command.Type.USER, "3. Try to steal coins")

        ).queue();
    }
}
