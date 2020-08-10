import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;

public interface APIInterface {
    ArrayList<HttpResponse<JsonNode>> makeRequest(String gamerTag, String platform) throws Exception;
}
