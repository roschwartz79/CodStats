import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
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
    public static Scanner scanner = new Scanner(System.in);

    public void updateAll() throws Exception {
        MongoCollection<Document> dataCollection = database.getCollection("Data");
        MongoCollection<Document> scoreCollection = database.getCollection("Scores");
        MongoCollection<Document> gamertagsCollection = database.getCollection("Gamertags");

        int len = (int) gamertagsCollection.countDocuments();

        System.out.print("Updating");

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

                System.out.print(".");
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

    
    public double[] updateAndGetScores(String updateTrends) throws JsonProcessingException {
        MongoCollection<Document> scoreCollection = database.getCollection("Scores");
        MongoCollection<Document> dataCollection = database.getCollection("Data");

        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<String> scoreList = new ArrayList();

        // Go through each doc in the collection
        for (Document cur : dataCollection.find().projection(fields(exclude("id"),excludeId()))) {
            // map json to a player
            Player player = objectMapper.readValue(cur.toJson(), Player.class);

            double oldScore = Double.parseDouble(scoreCollection.find(new Document("Gamertag", cur.get("GMTG"))).first().get("Score").toString());

            // get an updated score
            double score = Score.getScore(player);

            // configure trends
            String trend;
            if (oldScore > score){ trend = "DOWN"; }
            else if ( oldScore < score) { trend = "UP"; }
            else { trend = "NONE"; }

            // Update the new score into the db
            Document scoreDoc;
            if (updateTrends.equalsIgnoreCase("y")){ scoreDoc = new Document("Score", score).append("Trend", trend); }
            else { scoreDoc = new Document("Score", score); }
            scoreCollection.updateOne(eq("Gamertag",cur.get("GMTG")),new Document("$set", scoreDoc));


        }

        for (Document cur : scoreCollection.find().projection(fields(include("Gamertag", "Score", "Trend"), excludeId()))) {
            System.out.println(cur.toJson());
        }

        return null;
    }

    public void createDBGroup(){
        MongoCollection<Document> groupsCollection = database.getCollection("Groups");


        String groupName = null;
        ArrayList<String> gamertags = new ArrayList<>();

        boolean checked = true;
        while(checked) {
            System.out.println("Enter the name of the group: ");
            groupName = scanner.nextLine();

            boolean inColl = false;
            for (Document cur : groupsCollection.find()) {
                if (cur.get("Group").equals(groupName)){
                    System.out.println("This group has already been added to the database. Update or delete the group.");
                    inColl = true;
                }
            }

            if (!inColl) {
                checked = false;
            }

        } ;

        String userIn = "d";
        while  (userIn.equalsIgnoreCase("d")) {
            System.out.println("Add player gamertag to " + groupName + ",press D when you are finished, or enter Q to exit.");
            userIn = scanner.nextLine();

            if (userIn.equalsIgnoreCase("q")){ return; }

            String gamertag2add = scanner.nextLine();
            gamertags.add(gamertag2add);
        }

        // add the array to the collection
        groupsCollection.insertOne(new Document(groupName, gamertags.toArray()));
        System.out.println("Successfully created the " + groupName + " collection.");
    }

}