import java.util.ArrayList;

public interface PlayerInterface {
    public ArrayList<String> getPlayersCurrentlyTracked();
    public void addPlayerToTrack(String gamertag);
    public void createPlayerList();
}
