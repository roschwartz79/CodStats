import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;



public class Main {
    final static String[] GAMERTAGS = {"McDonalds79", "Cashius Playz", "dr treyy", "schoolboywu69", "Joevani JoJo"};
    final static int NUMOFGAMERTAGS = GAMERTAGS.length;

    public static void main(String[] args) throws Exception{

        HttpResponse<JsonNode>[] responses = new HttpResponse[5];

        // Make Api requests
        for(int i = 0; i < 5; i++) {
            responses[i] = makeRequest(GAMERTAGS[i]);
            TimeUnit.SECONDS.sleep(1);
        }
        String[] keys = {"kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed"};


        Object[][] playerData = new Object[GAMERTAGS.length][keys.length];

        System.out.println("Printing player data");
        for (int j = 0; j < 5; j++) {
            System.out.println("\n" + GAMERTAGS[j]);
            for (int i = 0; i < 7; i++) {
                playerData[j][i] = responses[j].getBody().getObject().getJSONObject("lifetime").getJSONObject("mode").getJSONObject("br").getJSONObject("properties").get(keys[i]);
                System.out.println(keys[i] + ": " + playerData[j][i]);
            }
        }

        // normalize the data
        double[][] normalizedData = normalizeData(playerData);
        for (int i = 0; i<normalizedData.length; i++){
            System.out.println();
            for (int j = 0; j<normalizedData[0].length; j++){
                System.out.print(normalizedData[i][j] + " ");
            }
        }

        // get the calculated scores
        double[] scores = calculateScores(normalizedData);

        System.out.println("/nPrinting out scores....");
        for (int p = 0; p < NUMOFGAMERTAGS; p++){
            System.out.println(GAMERTAGS[p] + "    " + scores[p]);
        }

    }

    // perform the api request and return all raw JSON data
    public static HttpResponse<JsonNode> makeRequest(String gamerTag) throws Exception{
        HttpResponse<JsonNode> response = Unirest.get("https://call-of-duty-modern-warfare.p.rapidapi.com/multiplayer/" + gamerTag +"/xbl")
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
    public static double[][] normalizeData(Object rawStats[][]){
        double normalizedStats[][] = new double[rawStats.length][rawStats[0].length];

        double rawStatsDouble[][] = new double[rawStats.length][rawStats[0].length];

        for(int i = 0; i<rawStats.length; i++){
            for (int j = 0; j < rawStats[0].length; j++){
                rawStatsDouble[i][j] = Double.valueOf(rawStats[i][j].toString());
            }
        }

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
    public static double[] calculateScores(double normalizedStats[][]){
        double scores[] = new double[normalizedStats.length];

        // just for shits add up each normalized stat to see what happens
        // for each player
        for(int player = 0; player < normalizedStats.length; player++){
            // add each stat
            double tempScore = 0;
            for (int col = 0; col < normalizedStats[0].length; col++){
                tempScore += normalizedStats[player][col];
            }
            scores[player] = tempScore;
        }

        return scores;
    }
}
