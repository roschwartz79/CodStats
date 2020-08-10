import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface FileInterface {
    void createFile() throws IOException;
    void readCachedFile() throws FileNotFoundException;
    void updatePlayerData();
    void addPlayerData(String Gamertag, String Platform) throws Exception;

    static ArrayList<Player> getPlayerList() {
        return null;
    }
}
