import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class DBHandler implements DBInterface {

    PlayerHandler playerHandler = new PlayerHandler();
    public static ArrayList<Player> playerList = new ArrayList<Player>();
    private static MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://USER_ROB_SCHWARTZ:"
                    + System.getenv("mongoDBcodpswd") +
                    "@cluster0.52wpn.mongodb.net/PlayerData?retryWrites=true&w=majority");

    private static MongoClient mongoClient = new MongoClient(uri);
    private MongoDatabase database = mongoClient.getDatabase("PlayerData");

    private final static String[] FIELDS = {"kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed",
            "downs", "contracts", "revives","kills", "killsPerGame", "objectiveTeamWiped", "avgLifeTime", "distanceTraveled", "headshotPercentage",
            "gulagKills", "damageDone", "damageTaken", "objectiveLastStandKill"};
    private final static int NUMOFFIELDS = FIELDS.length;


    //TODO: only update player data when the user requests an update
    public void updatePlayerData() {

    }

    public void addPlayerData(String gamertag, String platform) throws Exception {
         /**
         * FOR OFFLINE DB USE, USING LOCALHOST
         * MongoClient mongoClient = new MongoClient();
         * MongoDatabase database = mongoClient.getDatabase("PlayerData");
         * MongoCollection collection = database.getCollection("Gamertags");
         **/

        MongoCollection<Document> gamertagsCollection = database.getCollection("Gamertags");
        MongoCollection<Document> dataCollection = database.getCollection("Data");

        long numPlayers = gamertagsCollection.countDocuments();

        System.out.println("Receiving data...");
        ArrayList<HttpResponse<JsonNode>> response = APIHandler.makeRequest(gamertag, platform);

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

            // Add the player to the Database
            Document gamertags = new Document("id", (int)numPlayers).append("GMTG", gamertag).append("KD", playerData[0])
                    .append("SPM", playerData[1]).append("TP25", playerData[2])
                    .append("TP5", playerData[3]).append("TP", playerData[4])
                    .append("WINS", playerData[5]).append("GP", playerData[6])
                    .append("DWNS", playerData[7]).append("CNTR", playerData[8])
                    .append("REV", playerData[9]).append("KILL20", playerData[10])
                    .append("KPG", playerData[11]).append("TMWP", playerData[12])
                    .append("AVGLF", playerData[13]).append("DSTTRV", playerData[14])
                    .append("HDSHTPCT", playerData[15]).append("GLGKL", playerData[16])
                    .append("DMGD", playerData[17]).append("DMGT", playerData[18])
                    .append("LSTSTND", playerData[19]).append("PLT", platform);
            dataCollection.insertOne(gamertags);
            gamertagsCollection.insertOne(new Document("id", (int)numPlayers).append("Gamertag", gamertag));

            System.out.println("\nSuccessfully added " + gamertag + " to the database");
        }
        catch(Exception e){
            System.out.println("\nThere was an error adding " + gamertag + " on " + platform +".\nCheck for the correct gamertag and platform.");
        }
    }

    public void getPlayerData(String gamertag){
        try {
            MongoCollection<Document> gamertagsCollection = database.getCollection("Gamertags");
            MongoCollection<Document> dataCollection = database.getCollection("Data");

            Document gtag = gamertagsCollection.find(new Document("Gamertag", gamertag)).first();

            System.out.println("Found " + gamertag + " in the DB!");
            int id = Integer.parseInt(String.valueOf(gtag.get("id")));

            // Then look for the stats
            Document stats = dataCollection.find(eq("id", id)).first();

            // Print nicely- maybe fix this later to be even nicer who knows
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(stats.toJson());
            String prettyJsonString = gson.toJson(je);
            System.out.println(prettyJsonString);
        }
        catch (Exception e){
            System.out.println("Could not find the player in the DB. Please try again.");
        }
    }

    public void closeDBConn(){
        mongoClient.close();
    }


}