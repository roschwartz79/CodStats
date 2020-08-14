import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface DBInterface {
    void updatePlayerData();
    void addPlayerData(String Gamertag, String Platform) throws Exception;

    static ArrayList<Player> getPlayerList() {
        return null;
    }
}
