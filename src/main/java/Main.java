import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class Main {
    public static void main(String[] args) throws Exception{

        // Initialize gamertags
        String[] gamertags = {"McDonalds79", "Cashius Playz", "dr treyy", "schoolboywu69", "Joevani JoJo"};

        HttpResponse<JsonNode>[] responses = new HttpResponse[5];

        // Make Api requests
        for(int i = 0; i < 5; i++) {
            responses[i] = makeRequest(gamertags[i]);
            TimeUnit.SECONDS.sleep(1);
        }
        String[] keys = {"kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed"};


        Object[][] playerData = new Object[gamertags.length][keys.length];

        System.out.println("Printing player data");
        for (int j = 0; j < 5; j++) {
            System.out.println("\n" + gamertags[j]);
            for (int i = 0; i < 7; i++) {
                playerData[j][i] = responses[j].getBody().getObject().getJSONObject("lifetime").getJSONObject("mode").getJSONObject("br").getJSONObject("properties").get(keys[i]);
                System.out.println(keys[i] + ": " + playerData[j][i]);
            }
        }

        // normalize the data
        double[][] normalizedData = normalizeData(playerData);
        System.out.println(normalizedData[1][1]);
        System.out.println(normalizedData[2][2]);

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

        // for each stat
        for(int i = 0; i< rawStats.length; i++){
            // set a max val
            double maxVal = (Double)rawStats[0][i];
            // loop through each of the rows for each player to find max value
            for (int j = 1; j < rawStats[0].length; j++){
                System.out.println("test" + rawStats[j][i]);
                if ((Double)rawStats[i][j] > (Double)maxVal){
                    maxVal = (Double)rawStats[i][j];
                }
            }

            // normalize the data
            for (int j = 0; j< rawStats[0].length; j++){
                normalizedStats[i][j] = (Double)rawStats[i][j];
            }
        }

        return normalizedStats;
    }

    // calculate the new "score" for each player using normalized stats higher score -> better player
    public static double[] calculateScores(Object normalizedStats[][]){
        double scores[] = new double[normalizedStats.length];


        return scores;
    }
}
