package com.mateusz.jakuszko.roomforyou.opencagegeocoderapi;

import com.mateusz.jakuszko.roomforyou.opencagegeocoderapi.dto.GetGeoResponse;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
@AllArgsConstructor
public class JsonMapper {

    private final RestTemplate restTemplate;

    public Map<String, String> mapJsonRespondToGeometryValues(URL url) throws IOException, ParseException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        StringBuilder jsonRespond = new StringBuilder();
        Scanner sc = new Scanner(url.openStream());
        while (sc.hasNext()) {
            jsonRespond.append(sc.nextLine());
        }
        sc.close();
        JSONParser parse = new JSONParser();
        JSONObject fullJsonObject = (JSONObject) parse.parse(jsonRespond.toString());
        JSONArray jsonResultsArray = (JSONArray) fullJsonObject.get("results");
        JSONObject jsonObject = (JSONObject) ((JSONObject) jsonResultsArray.get(0)).get("geometry");
        Map<String, String> coordinatesMap = new HashMap<>();
        coordinatesMap.put("latitude",jsonObject.get("lat").toString());
        coordinatesMap.put("longitude",jsonObject.get("lng").toString());
        return coordinatesMap;
    }

    //TODO Resolve problem with wrong response
    public GetGeoResponse getResponse(String url) {
        return restTemplate.getForObject(url, GetGeoResponse.class);
    }
}

















