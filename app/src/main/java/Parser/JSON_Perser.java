package Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Flower;

/**
 * Created by ananth on 8/17/2016.
 */
public class JSON_Perser {
    public static List<Flower> parseFeed(String content) {
        List<Flower> flowerList = null;
        try {
            JSONArray jsonArray = new JSONArray(content);
            flowerList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                Flower flower = new Flower();

                flower.setProductId(jsonObj.getInt("productId"));
                flower.setCategory(jsonObj.getString("category"));
                flower.setName(jsonObj.getString("name"));
                flower.setInstructions(jsonObj.getString("instructions"));
                flower.setPrice(jsonObj.getDouble("price"));
                flower.setPhoto(jsonObj.getString("photo"));

                flowerList.add(flower);
            }
            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
