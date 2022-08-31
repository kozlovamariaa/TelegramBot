import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class Parser {
    static String url = "https://api.tinkoff.ru/geo/withdraw/clusters";
    static String jsonInputString = "{\"bounds\":{\"bottomLeft\":{\"lat\":55.45200245983143,\"lng\":37.277780755493026},\"topRight\":{\"lat\":55.96682824451231,\"lng\":37.985025628539894}},\"filters\":{\"banks\":[\"tcs\"],\"showUnavailable\":true,\"currencies\":[\"USD\"]},\"zoom\":13}";
    public String answer;
    public Distance distance;
    public int count = 0;
    public HashMap<String, String > listOfAtms1 = new HashMap<>();
    public final String USD = "USD";


    public String getJSON() throws IOException {
        URL myURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
                return responseLine;
            }
        }
        return "error";
    }

    public String view(String idRequest) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonText = getJSON();
        System.out.println(jsonText);
        Result pl = mapper.readValue(jsonText, Result.class);
        String result = createString(pl, idRequest);
        return result;
    }



    public String createString(Result result, String idRequest) {
        for (int i = 0; i < result.payload.clusters.length; i++) {
            for (int j = 0; j < result.payload.clusters[i].points.length; j++) {
                String var = result.payload.clusters[i].points[j].id;
                if (var.equals(idRequest)) {
                    for (int k = 0 ; k < result.payload.clusters[i].points[j].limits.length; k ++){
                        if (result.payload.clusters[i].points[j].limits[k].currency.equals(USD)) {
                            answer = String.valueOf(result.payload.clusters[i].points[j].limits[k].amount);
                            return answer + " usd";
                        }
                    }
                }
            }
        }
        return answer;
    }

    public HashMap<String, String> viewForList(Double latitude, Double longitude) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonText = getJSON();
        Result pl = mapper.readValue(jsonText, Result.class);
        listOfAtms1 = createListOfAtm(pl, latitude, longitude);
        return listOfAtms1;
    }

    public HashMap<String, String> createListOfAtm(Result result, Double latitude, Double longitude) {
        for (int i = 0; i < result.payload.clusters.length; i++) {
            for (int j = 0; j < result.payload.clusters[i].points.length; j++) {
                Double lat = Double.valueOf((result.payload.clusters[i].points[j].location.lat));
                Double lng = Double.valueOf((result.payload.clusters[i].points[j].location.lng));
                distance = new Distance(latitude, longitude, lat,lng);
                count++;
                System.out.println(count);
                if (distance.onDistance()) {
                    listOfAtms1.put(result.payload.clusters[i].points[j].id, result.payload.clusters[i].points[j].address);
                }
            }
        }
        return listOfAtms1;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Result{
    @JsonProperty Payload payload;
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Payload{
        @JsonProperty Clusters[] clusters;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Clusters{
        @JsonProperty Points[] points;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Points{
        @JsonProperty String id;
        @JsonProperty Location location;
        @JsonProperty Limits[] limits;
        @JsonProperty String address;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Limits{
        @JsonProperty String currency;
        @JsonProperty String amount;
    }
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Location{
        @JsonProperty String lat;
        @JsonProperty String lng;
    }

}



