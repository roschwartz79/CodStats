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
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;



public class Main {
    private final static String[] GAMERTAGS = {"McDonalds79", "Cashius Playz", "dr treyy", "schoolboywu69", "Joevani JoJo", "kelloggking","Ccatania21"};
    private final static String[] PLATFORM = {"xbl", "xbl", "xbl", "xbl", "xbl", "xbl", "xbl"};
    private final static int NUMOFGAMERTAGS = GAMERTAGS.length;
    private final static String[] FIELDS = {"kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed"};
    private final static int NUMOFFIELDS = FIELDS.length;
    private final static int MODE = 1; // 0 is OFFLINE 1 is ONLINE
    private static ArrayList<Player> playerList = new ArrayList<Player>();

    public static void main(String[] args) throws Exception{

        if (MODE == 1) {
            HttpResponse<JsonNode>[] responses = new HttpResponse[NUMOFGAMERTAGS];

            // Make Api requests
            for (int i = 0; i < NUMOFGAMERTAGS; i++) {
                responses[i] = makeRequest(GAMERTAGS[i], PLATFORM[i]);
                TimeUnit.SECONDS.sleep(1);
            }

            double[][] playerData = new double[GAMERTAGS.length][FIELDS.length];

            System.out.println("Printing player data");
            for (int j = 0; j < NUMOFGAMERTAGS; j++) {
                System.out.println("\n" + GAMERTAGS[j]);
                for (int i = 0; i < NUMOFFIELDS; i++) {
                    playerData[j][i] = Double.valueOf(responses[j].getBody().getObject().getJSONObject("lifetime").getJSONObject("mode").getJSONObject("br").getJSONObject("properties").get(FIELDS[i]).toString());
                    System.out.println(FIELDS[i] + ": " + playerData[j][i]);
                }

                // add players as the raw data is selected
                playerList.add(new Player(playerData[j][0], playerData[j][1], (int)(playerData[j][2]), (int)playerData[j][3],
                        playerData[j][4], (int)playerData[j][5], (int)playerData[j][6],GAMERTAGS[j], PLATFORM[j]));
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
        int cols = NUMOFFIELDS;
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
    private static HttpResponse<JsonNode> makeRequest(String gamerTag, String platform) throws Exception{
        HttpResponse<JsonNode> response = Unirest.get("https://call-of-duty-modern-warfare.p.rapidapi.com/multiplayer/" + gamerTag +"/" + platform)
                .header("x-rapidapi-host", "call-of-duty-modern-warfare.p.rapidapi.com")
                .header("x-rapidapi-key", System.getenv("rapidAPIKey"))
                .asJson();


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        //String prettyJsonString = gson.toJson(je);
        //System.out.println(prettyJsonString);

        return response;
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
            double kd = playerList.get(player).getKd() * 250;
            System.out.println("KD*250: " + kd);
            // score per minute
            double spm = playerList.get(player).getSpm() * 5;
            System.out.println("SPM*5: " + spm);

            // top 25 / time played
            double top25Time = playerList.get(player).getTop25()/timePlayed * 100;
            System.out.println("top25/time * 100: " + top25Time);

            // top 25 / gamesPlayed
            double top25Games = playerList.get(player).getTop25()/gamesPlayed * 100;
            System.out.println("top25/games * 100: " + top25Games);

            // top 5 / time played
            double top5Time = playerList.get(player).getTop5()/timePlayed * 100;
            System.out.println("Top 5/time * 100: " + top5Time);

            // top 5 / gamesPlayed * 10
            double top5Games = playerList.get(player).getTop5()/gamesPlayed * 100;
            System.out.println("Top 25/games * 10: " + top5Games);

            // wins / timePlayed * 10
            double winsTime = playerList.get(player).getWins()/timePlayed * 200;
            System.out.println("Wins/time * 10: "+winsTime);

            // wins / gamesPlayed * 20
            double winsGames = playerList.get(player).getWins()/gamesPlayed * 200;
            System.out.println("wins/games * 200: " + winsGames);

            // kd / gamesPlayed * 2000
            double kdGames = playerList.get(player).getKd()/gamesPlayed * 2000;
            System.out.println("kd/games * 2000: " + kdGames);

            System.out.println("DATA END");
            // ADD UP THE SCORES
            scores[player] = kd + spm + top25Time + top25Games + top5Time
                    + top5Games + winsTime + winsGames + kdGames;
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
