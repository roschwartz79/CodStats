import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import javax.swing.*;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;



public class Main {
    // TODO make an intput file with the gamertags and plat
    private final static String[] GAMERTAGS = {"McDonalds79", "Cashius Playz", "dr treyy", "schoolboywu69", "Joevani JoJo", "kelloggking","Crackrockjack", "Ccatania21"};
    private final static String[] PLATFORM = {"xbl", "xbl", "xbl", "xbl", "xbl", "xbl", "xbl", "xbl"};
    private final static int NUMOFGAMERTAGS = GAMERTAGS.length;
    private final static String[] MULTIFIELDS = {"kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed",
            "downs", "contracts", "revives"};
    private final static int NUMOFMUTLIFIELDS = MULTIFIELDS.length;
    private final static String[] WZFIELDS = {"kills", "killsPerGame", "objectiveTeamWiped", "avgLifeTime", "distanceTraveled", "headshotPercentage",
            "gulagKills", "damageDone", "damageTaken", "objectiveLastStandKill"};
    private static int NUMOFWZFIELDS = WZFIELDS.length;
    private final static int MODE = 1; // 0 is OFFLINE 1 is ONLINE
    private static ArrayList<Player> playerList = new ArrayList<Player>();

    public static void main(String[] args) throws Exception{

        if (MODE == 1) {
            ArrayList<ArrayList<HttpResponse<JsonNode>>> responses = new ArrayList<>(NUMOFGAMERTAGS);

            // Temp array to store player data in before making a new player
            double[] playerData = new double[NUMOFMUTLIFIELDS + NUMOFWZFIELDS];

            // Make Api requests, plumb data
            for (int i = 0; i < NUMOFGAMERTAGS; i++) {
                responses.add(makeRequest(GAMERTAGS[i], PLATFORM[i]));
                for (int j = 0; j < NUMOFMUTLIFIELDS; j++){
                    // Data from the Multi API endpoint
                    playerData[j] = Double.parseDouble(responses.get(i).get(0).getBody().getObject().getJSONObject("lifetime").getJSONObject("mode").
                            getJSONObject("br").getJSONObject("properties").get(MULTIFIELDS[j]).toString());
                    // Data from the WZ API endpoint
                    playerData[j+10] = Double.parseDouble(responses.get(i).get(1).getBody().getObject().getJSONObject("summary").getJSONObject("all").
                            get(WZFIELDS[j]).toString());
                }

                //add the players to the playerList
                playerList.add(new Player(playerData[0], playerData[1], (int)playerData[2], (int)playerData[3], playerData[4], (int)playerData[5],
                        (int)playerData[6], (int)playerData[7], (int)playerData[8], (int)playerData[9], playerData[10], playerData[11], playerData[12],
                        playerData[13], playerData[14], playerData[15], (int)playerData[16], playerData[17], playerData[18], (int)playerData[19],
                        GAMERTAGS[i], PLATFORM[i]));
            }
        }

        // TODO: Make offline data usable again, does not work with new implementation
        else{
            // OR read in a (CURRENTLY) normalized only file for offline use

        }

        // get the calculated scores
        double[] scores = calculateScores();

        // Print data to command line
        System.out.println("\n\nPrinting out scores....");
        for (int p = 0; p < NUMOFGAMERTAGS; p++){
            System.out.println(GAMERTAGS[p] + "            " + scores[p]);
        }

        // Print the data
        printScores(scores);

    }

    private static double[][] readInFile() throws Exception{
        // read in the stats for offline testing
        Scanner scanner = new Scanner(new BufferedReader(new FileReader("stats.txt")));
        int cols = NUMOFMUTLIFIELDS;
        int rows = NUMOFGAMERTAGS;
        double[][] normalizedData = new double[rows][cols];
        while(scanner.hasNextLine()){
            for (int i = 0; i < rows; i++){
                String[] line = scanner.nextLine().trim().split(" ");
                for (int j = 0; j< cols; j++){
                    normalizedData[i][j] = Double.parseDouble(line[j]);
                }
            }
        }

        return normalizedData;
    }

    // perform the api request and return all raw JSON data
    private static ArrayList<HttpResponse<JsonNode>> makeRequest(String gamerTag, String platform) throws Exception{
        ArrayList<HttpResponse<JsonNode>> responseList = new ArrayList<>();

        HttpResponse<JsonNode> multiplayerResponse = Unirest.get("https://call-of-duty-modern-warfare.p.rapidapi.com/multiplayer/" + gamerTag +"/" + platform)
                .header("x-rapidapi-host", "call-of-duty-modern-warfare.p.rapidapi.com")
                .header("x-rapidapi-key", System.getenv("rapidAPIKey"))
                .asJson();

        responseList.add(multiplayerResponse);

        TimeUnit.SECONDS.sleep(1);

        HttpResponse<JsonNode> warzoneResponse = Unirest.get("https://call-of-duty-modern-warfare.p.rapidapi.com/warzone-matches/" + gamerTag + "/" + platform)
                .header("x-rapidapi-host", "call-of-duty-modern-warfare.p.rapidapi.com")
                .header("x-rapidapi-key", System.getenv("rapidAPIKey"))
                .asJson();

        TimeUnit.SECONDS.sleep(1);

        responseList.add(warzoneResponse);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(multiplayerResponse.getBody().toString());
        //String prettyJsonString = gson.toJson(je);
        //System.out.println(prettyJsonString);

        return responseList;
    }

    // calculate the new "score" for each player using normalized stats higher score -> better player
    // "kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed"
    private static double[] calculateScores(){
        double scores[] = new double[playerList.size()];

        // for each player
        for(int player = 0; player < playerList.size(); player++){
            // Get time and games played
            double timePlayed = playerList.get(player).getTimePlayed();
            double gamesPlayed = playerList.get(player).getGamesPlayed();


            System.out.println("\nDATA START FOR " + playerList.get(player).getGamertag());
            // kd ratio
            double kd = playerList.get(player).getKd() * 500;
            System.out.println("KD*500: " + kd);
            // score per minute
            double spm = playerList.get(player).getSpm() * 5;
            System.out.println("SPM*5: " + spm);

            // top 25 / time played
            double top25Time = playerList.get(player).getTop25()/timePlayed * 100000;
            System.out.println("top25/time * 100000: " + top25Time);

            // top 25 / gamesPlayed
            double top25Games = playerList.get(player).getTop25()/gamesPlayed * 100;
            System.out.println("top25/games * 100: " + top25Games);

            // top 5 / time played
            double top5Time = playerList.get(player).getTop5()/timePlayed * 500000;
            System.out.println("Top 5/time * 500000: " + top5Time);

            // top 5 / gamesPlayed * 10
            double top5Games = playerList.get(player).getTop5()/gamesPlayed * 500;
            System.out.println("Top 5/games * 500: " + top5Games);

            // wins / timePlayed * 10
            double winsTime = playerList.get(player).getWins()/timePlayed * 500000;
            System.out.println("Wins/time * 500000: "+winsTime);

            // wins / gamesPlayed * 20
            double winsGames = playerList.get(player).getWins()/gamesPlayed * 500;
            System.out.println("wins/games * 200: " + winsGames);

            // kd / gamesPlayed * 2000
            double kdGames = playerList.get(player).getKd()/gamesPlayed * 2000;
            System.out.println("kd/games * 2000: " + kdGames);

            // headshot percentage * 10000
            double headshotPercentage = playerList.get(player).getHeadshotPercentage() * 5000;
            System.out.println("headshot percentage * 5000: " + headshotPercentage);

            // average lifetime
            double avgLife = playerList.get(player).getAvgLifetime();
            System.out.println("AvgLife: " + avgLife);

            // damage done / 50
            double damageDone = playerList.get(player).getDamageDone() / 50;
            System.out.println("damagedone/50: " + damageDone);

            // team wipes * 10
            double teamWipes = playerList.get(player).getObjectiveTeamWiped() * 10;
            System.out.println("team wipes*10: " + teamWipes);

            System.out.println("DATA END");
            // ADD UP THE SCORES
            scores[player] = kd + spm + top25Time + top25Games + top5Time
                    + top5Games + winsTime + winsGames + kdGames + headshotPercentage
                    + avgLife + damageDone;
        }

        return scores;
    }

    // Print out the data
    public static void printScores(double[] scores) throws PrinterException {
        String[] columnNames = {"Name", "Score"};
        Object[][] data = new Object[NUMOFGAMERTAGS][2];
        for(int i = 0; i<NUMOFGAMERTAGS; i++){
            data[i][0] = GAMERTAGS[i];
            data[i][1] = scores[i];
        }

        JFrame jFrame=new JFrame();
        JTable jTable = new JTable(data, columnNames);
        jTable.setBounds(30,40,200,300);
        JScrollPane sp=new JScrollPane(jTable);
        jFrame.add(sp);
        jFrame.setSize(400,400);
        jFrame.setVisible(true);
    }
}
