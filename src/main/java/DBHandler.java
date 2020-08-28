import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;


public class DBHandler implements DBInterface {
    public static ArrayList<Player> playerList = new ArrayList<Player>();
    private static final MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://USER_ROB_SCHWARTZ:"
                    + System.getenv("mongoDBcodpswd") +
                    "@cluster0.52wpn.mongodb.net/PlayerData?retryWrites=true&w=majority");

    private static final MongoClient mongoClient = new MongoClient(uri);
    private final MongoDatabase database = mongoClient.getDatabase("PlayerData");

    private final static String[] FIELDS = {"kdRatio", "scorePerMinute", "topTwentyFive", "topFive", "timePlayed", "wins", "gamesPlayed",
            "downs", "contracts", "revives","kills", "killsPerGame", "objectiveTeamWiped", "avgLifeTime", "distanceTraveled", "headshotPercentage",
            "gulagKills", "damageDone", "damageTaken", "objectiveLastStandKill"};
    private final static int NUMOFFIELDS = FIELDS.length;

    public void updateAll() throws Exception {
        MongoCollection<Document> dataCollection = database.getCollection("Data");
        MongoCollection<Document> scoreCollection = database.getCollection("Scores");
        MongoCollection<Document> gamertagsCollection = database.getCollection("Gamertags");

        int len = (int) gamertagsCollection.countDocuments();

        System.out.println("Receiving updated data...");

        for (int i = 0; i < len; i++) {
            Document curDoc = gamertagsCollection.find(new Document("id", i)).first();
            String gamertag = String.valueOf(curDoc.get("Gamertag"));

            Document pltDoc = dataCollection.find(new Document("GMTG", gamertag)).first();
            String platform = String.valueOf(pltDoc.get("PLT"));

            ArrayList<HttpResponse<JsonNode>> response = APIHandler.makeRequest(gamertag, platform);

            double[] playerData = new double[NUMOFFIELDS];


            try {
                for (int j = 0; j < NUMOFFIELDS / 2; j++) {
                    // Data from the Multi API endpoint
                    playerData[j] = Double.parseDouble(response.get(0).getBody().getObject().getJSONObject("lifetime").getJSONObject("mode").
                            getJSONObject("br").getJSONObject("properties").get(FIELDS[j]).toString());
                    // Data from the WZ API endpoint
                    playerData[j + 10] = Double.parseDouble(response.get(1).getBody().getObject().getJSONObject("summary").getJSONObject("all").
                            get(FIELDS[j + 10]).toString());
                }

                // Add the player to the Database
                Document data = new Document("GMTG", gamertag).append("KD", playerData[0])
                        .append("SPM", playerData[1]).append("TP25", playerData[2])
                        .append("TP5", playerData[3]).append("TP", playerData[4])
                        .append("WINS", playerData[5]).append("GP", playerData[6])
                        .append("DWNS", playerData[7]).append("CNTR", playerData[8])
                        .append("REV", playerData[9]).append("KILL20", playerData[10])
                        .append("KPG", playerData[11]).append("TMWP", playerData[12])
                        .append("AVGLF", playerData[13]).append("DSTTRV", playerData[14])
                        .append("HDSHTPCT", playerData[15]).append("GLGKL", playerData[16])
                        .append("DMGD", playerData[17]).append("DMGT", playerData[18])
                        .append("LSTSTND", playerData[19]);
                dataCollection.updateOne(eq("GMTG", gamertag), new Document("$set", data));

                System.out.println("Added to database");

                // Create player object to calculate a score from
                Player insertedPlayer = new Player((playerData[0]), (playerData[1]), (playerData[2]),
                        (playerData[3]), (playerData[4]), (playerData[5]),
                        (playerData[6]), (playerData[7]), (playerData[8]),
                        (playerData[9]), (playerData[10]), (playerData[11]),
                        (playerData[12]), (playerData[13]), (playerData[14]),
                        (playerData[15]), (playerData[16]), (playerData[17]),
                        (playerData[18]), (playerData[19]), gamertag, platform);

                System.out.println("inserting");

                double score = Score.getScore(insertedPlayer);

                System.out.println("Got score");

                // Insert into the document
                Document scoreDoc = new Document("Score", score);
                scoreCollection.updateOne(eq("Gamertag",gamertag),new Document("$set", scoreDoc));

                System.out.println("has been inserted");

                System.out.println("\nSuccessfully updated " + gamertag + " in the database");
            } catch (Exception e) {
                System.out.println("\nThere was an error updating " + gamertag + ".\nTry again in a litte. ");
                return;
            }
        }
    }


    public void updatePlayerData(String gamertag, String platform) throws Exception {
        MongoCollection<Document> dataCollection = database.getCollection("Data");
        MongoCollection<Document> scoreCollection = database.getCollection("Scores");

        System.out.println("Receiving updated data...");
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
            Document data = new Document("GMTG", gamertag).append("KD", playerData[0])
                    .append("SPM", playerData[1]).append("TP25", playerData[2])
                    .append("TP5", playerData[3]).append("TP", playerData[4])
                    .append("WINS", playerData[5]).append("GP", playerData[6])
                    .append("DWNS", playerData[7]).append("CNTR", playerData[8])
                    .append("REV", playerData[9]).append("KILL20", playerData[10])
                    .append("KPG", playerData[11]).append("TMWP", playerData[12])
                    .append("AVGLF", playerData[13]).append("DSTTRV", playerData[14])
                    .append("HDSHTPCT", playerData[15]).append("GLGKL", playerData[16])
                    .append("DMGD", playerData[17]).append("DMGT", playerData[18])
                    .append("LSTSTND", playerData[19]);
            dataCollection.updateOne(eq("GMTG", gamertag), new Document("$set", data));

            // Create player object to calculate a score from
            Player insertedPlayer = new Player((playerData[0]), (playerData[1]), (playerData[2]),
                    (playerData[3]), (playerData[4]), (playerData[5]),
                    (playerData[6]), (playerData[7]), (playerData[8]),
                    (playerData[9]), (playerData[10]), (playerData[11]),
                    (playerData[12]), (playerData[13]), (playerData[14]),
                    (playerData[15]), (playerData[16]), (playerData[17]),
                    (playerData[18]), (playerData[19]), gamertag, platform);


            double score = Score.getScore(insertedPlayer);

            // Insert into the document
            Document scoreDoc = new Document("Score", score);
            scoreCollection.updateOne(eq("Gamertag",gamertag),new Document("$set", scoreDoc));


            System.out.println("\nSuccessfully updated " + gamertag + " in the database");
        }
        catch(Exception e){
            System.out.println("\nThere was an error updating " + gamertag + " on " + platform +".\nCheck for the correct gamertag and platform.");
        }
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
        MongoCollection<Document> scoreCollection = database.getCollection("Scores");

        for (Document cur : gamertagsCollection.find().projection(fields(include("Gamertag", "Score"), excludeId()))) {
            if (cur.get("Gamertag").equals(gamertag)){
                System.out.println("This gamertag has already been added to the database.");
                return;
            }
        }

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

            // Create player object to calculate a score from
            Player insertedPlayer = new Player((playerData[0]), (playerData[1]), (playerData[2]),
                    (playerData[3]), (playerData[4]), (playerData[5]),
                    (playerData[6]), (playerData[7]), (playerData[8]),
                    (playerData[9]), (playerData[10]), (playerData[11]),
                    (playerData[12]), (playerData[13]), (playerData[14]),
                    (playerData[15]), (playerData[16]), (playerData[17]),
                    (playerData[18]), (playerData[19]), gamertag, platform);

            double score = Score.getScore(insertedPlayer);
            // Insert into the document
            scoreCollection.insertOne(new Document("Gamertag", gamertag).append("id", (int)numPlayers).append("Score", score));

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

    
    public double[] getScores(){
        MongoCollection<Document> scoreCollection = database.getCollection("Scores");

        for (Document cur : scoreCollection.find().projection(fields(include("Gamertag", "Score"), excludeId()))) {
            System.out.println(cur.toJson());
        }

        return null;
    }


}