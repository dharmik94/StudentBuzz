package com.app.dev.studentbuzz;

/**
 * Created by dharmik on 02-05-2016.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {

    public static List<String> Parse(String json){
        try {
            List<String> classList = new ArrayList<>();
            JSONObject jsonResponse = new JSONObject(json);
            JSONArray cast = jsonResponse.getJSONArray("class");
            for (int i=0; i<cast.length(); i++) {
                JSONObject actor = cast.getJSONObject(i);
                classList.add(actor.getString("c_name"));
            }
            return classList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}