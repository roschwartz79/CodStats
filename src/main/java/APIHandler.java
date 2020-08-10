import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class APIHandler implements APIInterface{

    // perform the api request and return all raw JSON data
    public ArrayList<HttpResponse<JsonNode>> makeRequest(String gamerTag, String platform) throws Exception{
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

        //------- FOR DEBUGGING -------//
        //String prettyJsonString = gson.toJson(je);
        //System.out.println(prettyJsonString);

        return responseList;
    }
}
