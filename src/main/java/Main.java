import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;



public class Main {
    private final static String[] GAMERTAGS = {"McDonalds79", "Cashius Playz", "dr treyy", "schoolboywu69", "Joevani JoJo"};
    private final static String[] PLATFORM = {"xbl", "xbl", "xbl", "xbl", "xbl"};
    private final static int NUMOFGAMERTAGS = GAMERTAGS.length;
    private final static String[] FIELDS = {"kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed"};
    private final static int NUMOFFIELDS = FIELDS.length;
    private final static int MODE = 0; // 0 is OFFLINE 1 is ONLINE


    public static void main(String[] args) throws Exception{

        double[][] normalizedData;
        ArrayList<Player> playerList = new ArrayList<Player>();

        if (MODE == 1) {
            HttpResponse<JsonNode>[] responses = new HttpResponse[5];

            // Make Api requests
            for (int i = 0; i < 5; i++) {
                responses[i] = makeRequest(GAMERTAGS[i], PLATFORM[i]);
                TimeUnit.SECONDS.sleep(1);
            }

            double[][] playerData = new double[GAMERTAGS.length][FIELDS.length];

            System.out.println("Printing player data");
            for (int j = 0; j < NUMOFGAMERTAGS; j++) {
                System.out.println("\n" + GAMERTAGS[j]);
                for (int i = 0; i < 7; i++) {
                    playerData[j][i] = Double.valueOf(responses[j].getBody().getObject().getJSONObject("lifetime").getJSONObject("mode").getJSONObject("br").getJSONObject("properties").get(FIELDS[i]).toString());
                    System.out.println(FIELDS[i] + ": " + playerData[j][i]);
                }

                //TODO Create player objects with raw data from the api & include data from last 20 games

                // add players as the raw data is selected
                playerList.add(new Player(playerData[j][0], playerData[j][1], (int)(playerData[j][1]), (int)playerData[j][1],
                        playerData[j][1], (int)playerData[j][1], (int)playerData[j][1],GAMERTAGS[j], PLATFORM[j]));
            }

            // TODO double check all normalized data is being kept separate- some data we want normalized some we don't
            // normalize the data
            normalizedData = normalizeData(playerData);
            for (double[] normalizedDatum : normalizedData) {
                System.out.println();
                for (int j = 0; j < normalizedData[0].length; j++) {
                    System.out.print(normalizedDatum[j] + " ");
                }
            }

        }
        else{
            // OR read in a (CURRENTLY) normalized only file for offline use
            normalizedData = readInFile();
        }

        // get the calculated scores
        double[] scores = calculateScores(normalizedData);

        System.out.println("/nPrinting out scores....");
        for (int p = 0; p < NUMOFGAMERTAGS; p++){
            System.out.println(GAMERTAGS[p] + "    " + scores[p]);
        }

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

    // return the normalized stats
    // players are each row and stats are each column
    private static double[][] normalizeData(double rawStats[][]){
        double[][] normalizedStats = new double[rawStats.length][rawStats[0].length];

        double[][] rawStatsDouble = new double[rawStats.length][rawStats[0].length];


        // for each stat
        for(int i = 0; i< rawStatsDouble[0].length; i++){
            // set a max val
            double maxVal = rawStatsDouble[0][i];
            // loop through each of the rows for each player to find max value
            for (int j = 1; j < rawStatsDouble.length; j++){
                if (rawStatsDouble[j][i] > maxVal){
                    maxVal = rawStatsDouble[j][i];
                }
            }

            // normalize the data
            for (int j = 0; j< rawStats.length; j++){
                normalizedStats[j][i] = rawStatsDouble[j][i]/maxVal;
            }
        }

        return normalizedStats;
    }

    // calculate the new "score" for each player using normalized stats higher score -> better player
    // "kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed"
    private static double[] calculateScores(double normalizedStats[][]){
        double scores[] = new double[normalizedStats.length];

        // for each player
        for(int player = 0; player < normalizedStats.length; player++){
            // kd ratio * 10
            double kd = normalizedStats[player][0] * 10.0;

            // score per minute * 10
            double spm = normalizedStats[player][1] * 10.0;

            // top 25 / time played * 5
            double top25Time = normalizedStats[player][2] / normalizedStats[player][4] * 5.0;

            // top 25 / gamesPlayed * 5
            double top25Games = normalizedStats[player][2] / normalizedStats[player][6] * 5.0;

            // top 25 / time played * 10
            double top5Time = normalizedStats[player][3] / normalizedStats[player][4] * 10.0;

            // top 25 / gamesPlayed * 10
            double top5Games = normalizedStats[player][3] / normalizedStats[player][6] * 15.0;

            // wins / timePlayed * 10
            double winsTime = normalizedStats[player][5] / normalizedStats[player][4] * 15.0;

            // wins / gamesPlayed * 20
            double winsGames = normalizedStats[player][5] / normalizedStats[player][6] * 25.0;

            // kd / gamesPlayed * 20
            double kdGames = normalizedStats[player][0] / normalizedStats[player][6] * 20.0;

            // reward higher kd WITH a higher percentage of top 5 and top 25 finishes
            double kd25 = (kd * 5.0) / (normalizedStats[player][2]/normalizedStats[player][6]);

            double kd5 = (kd * 5.0) / (normalizedStats[player][3]/normalizedStats[player][6]);

            // ADD UP THE SCORES
            scores[player] = kd + spm + top25Time + top25Games + top5Time
                    + top5Games + winsTime + winsGames + kdGames + kd25 + kd5;
        }

        return scores;
    }
}
