package data.storage.guilds;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class GuildSetRepository {
    private static final String PATH = "bin/data.bin";

    public static void saveGuilds(List<GuildSet> guilds) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH));
            oos.writeObject(guilds);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            System.out.println("No file!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<GuildSet> readGuilds() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH));
            List<GuildSet> guilds = (List<GuildSet>) ois.readObject();
            ois.close();
            System.out.println("Guilds Initiated!");
            return guilds;
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.println("No Guilds Initiated!");
        return new LinkedList<>();
    }
}
