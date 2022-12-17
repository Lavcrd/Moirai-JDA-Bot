package data.configuration;

public class Configuration {

    //Variables
    private static final String token = "BOT_TOKEN_HERE";
    private static final String botOwnerID = "OWNER_ACC_ID_HERE";
    private static final String botAccountID = "BOT_USER_ACC_ID_HERE";
    private static final String botInviteLink = "BOT_INVITE_LINK_HERE";

    //Getters
    public static String getToken() {
        return token;
    }

    public static String getBotInviteLink() {
        return botInviteLink;
    }

    public static String getBotMention() {
        return "<@" + botAccountID + ">";
    }

    public static String getBotOwnerID() {
        return botOwnerID;
    }

    public static String getBotAccountID() {
        return botAccountID;
    }
}
