import java.util.ArrayList;

public class PlayerHandler implements PlayerInterface{
    public ArrayList<String> playersCurrentlyTracked = new ArrayList<>();
    public static ArrayList<Player> playerList = new ArrayList<Player>();

    public ArrayList<String> getPlayersCurrentlyTracked(){
        return playersCurrentlyTracked;
    }

    public void addPlayerToTrack(String gamertag){
        playersCurrentlyTracked.add(gamertag);
        return;
    }

    // TODO: When the program creates a new player list, it should be done through here
    public void createPlayerList() {

    }

}
