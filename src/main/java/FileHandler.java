import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.sun.tools.jdeprscan.scan.Scan;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler implements FileInterface{
    File file = new File("playerdata.txt");
    APIHandler apiHandler = new APIHandler();
    PlayerHandler playerHandler = new PlayerHandler();
    public static ArrayList<Player> playerList = new ArrayList<Player>();


    private static String[] GAMERTAGS = {"McDonalds79", "Cashius Playz", "dr treyy", "schoolboywu69", "Joevani JoJo", "kelloggking","Crackrockjack", "Ccatania21"};
    private  static String[] PLATFORM = {"xbl", "xbl", "xbl", "xbl", "xbl", "xbl", "xbl", "xbl"};
    private final static int NUMOFGAMERTAGS = GAMERTAGS.length;
    private final static String[] FIELDS = {"kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed",
            "downs", "contracts", "revives","kills", "killsPerGame", "objectiveTeamWiped", "avgLifeTime", "distanceTraveled", "headshotPercentage",
            "gulagKills", "damageDone", "damageTaken", "objectiveLastStandKill"};
    private final static int NUMOFFIELDS = FIELDS.length;

    public void createFile() throws IOException {
        //File file = new File("playerdata.txt");
        file.createNewFile();
        if(file.exists() && file.canRead() && file.canWrite()) {
            System.out.println("playerstats.txt created succesfully!");
        }
        else{
            System.out.println("File not created successfully. Check permissions.");
        }
    }

    public void readCachedFile() throws FileNotFoundException {
        if (file.exists()) {
            Scanner myReader = new Scanner(file);
            // go through each line (player) and create a player object
            while (myReader.hasNextLine()) {
                String playerData = myReader.nextLine();
                String[] splitData = playerData.split(",");
                playerList.add(new Player(Double.parseDouble(splitData[0]), Double.parseDouble(splitData[1]), Double.parseDouble(splitData[2]),
                        Double.parseDouble(splitData[3]), Double.parseDouble(splitData[4]), Double.parseDouble(splitData[5]),
                        Double.parseDouble(splitData[6]), Double.parseDouble(splitData[7]), Double.parseDouble(splitData[8]),
                        Double.parseDouble(splitData[9]), Double.parseDouble(splitData[10]), Double.parseDouble(splitData[11]),
                        Double.parseDouble(splitData[12]), Double.parseDouble(splitData[13]), Double.parseDouble(splitData[14]),
                        Double.parseDouble(splitData[15]), Double.parseDouble(splitData[16]), Double.parseDouble(splitData[17]),
                        Double.parseDouble(splitData[18]), Double.parseDouble(splitData[19]), splitData[20], splitData[21]));
                playerHandler.addPlayerToTrack(splitData[20]);
            }
            myReader.close();
        }
        else{
            System.out.println("File not found. Check that a file has been created.");
        }

    }

    //TODO: only update player data when the user requests an update
    public void updatePlayerData() {

    }

    public void addPlayerData(String gamertag, String platform) throws Exception {
        // If the player has already been tracked
        if (playerHandler.getPlayersCurrentlyTracked().contains(gamertag)){
            System.out.println("This player is already being tracked.");
            return;
        }

        ArrayList<HttpResponse<JsonNode>> response = apiHandler.makeRequest(gamertag, platform);

        double[] playerData = new double[NUMOFFIELDS];

        try {
            for (int j = 0; j < NUMOFFIELDS/2; j++) {
                // Data from the Multi API endpoint
                playerData[j] = Double.parseDouble(response.get(0).getBody().getObject().getJSONObject("lifetime").getJSONObject("mode").
                        getJSONObject("br").getJSONObject("properties").get(FIELDS[j]).toString());
                // Data from the WZ API endpoint
                playerData[j + 10] = Double.parseDouble(response.get(1).getBody().getObject().getJSONObject("summary").getJSONObject("all").
                        get(FIELDS[j+10]).toString());
            }

            // Add the player to the current playerList
            playerList.add(new Player(playerData[0], playerData[1], (int) playerData[2], (int) playerData[3], playerData[4], (int) playerData[5],
                    (int) playerData[6], (int) playerData[7], (int) playerData[8], (int) playerData[9], playerData[10], playerData[11], playerData[12],
                    playerData[13], playerData[14], playerData[15], (int) playerData[16], playerData[17], playerData[18], (int) playerData[19],
                    gamertag, platform));

            PrintWriter output = new PrintWriter(new FileWriter(file, true));
            output.flush();
            output.println(playerData[0]+","+playerData[1]+","+playerData[2]+","+playerData[3]+","+playerData[4]+","+playerData[5]+","+
                            playerData[6]+","+playerData[7]+","+playerData[8]+","+playerData[9]+","+playerData[10]+","+playerData[11]+","+
                            playerData[12]+","+playerData[13]+","+playerData[14]+","+playerData[15]+","+playerData[16]+","+playerData[17]+","+
                            playerData[8]+","+playerData[19]+","+gamertag+","+platform);
            output.flush();
            output.close();
            System.out.println("\nSuccessfully added " + gamertag + " to player data");


        }
        catch(Exception e){
            System.out.println("\nThere was an error adding " + gamertag + " on " + platform +".\nCheck for the correct gamertag and platform.");
        }
        // add the player to be tracked
        playerHandler.addPlayerToTrack(gamertag);

    }

    public static ArrayList<Player> getPlayerList() {
        return playerList;
    }
}