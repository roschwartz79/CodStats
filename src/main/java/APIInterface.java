import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;

public interface APIInterface {
    static ArrayList<HttpResponse<JsonNode>> makeRequest(String gamerTag, String platform) throws Exception {
        return null;
    }
}
