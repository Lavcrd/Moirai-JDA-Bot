package data.storage.users;

import data.storage.guilds.GuildSet;
import net.dv8tion.jda.api.entities.User;

public class UserSetRetrieve {
    public static UserSet getUserSet(GuildSet guild, User user) {
        for (UserSet userSet: guild.getUserSets()) {
            if(user.getId().equals(userSet.getUserId())) {
                updateUserSet(userSet, user);
                return userSet;
            }
        }
        UserSet newUser = new UserSet(user);
        guild.getUserSets().add(newUser);
        return newUser;
    }
    private static void updateUserSet(UserSet userSet, User user) {
        if (!user.getName().equals(userSet.getName())) userSet.setName(user.getName());
        if (!user.getDiscriminator().equals(userSet.getDiscriminator())) userSet.setDiscriminator(user.getDiscriminator());
    }
}
