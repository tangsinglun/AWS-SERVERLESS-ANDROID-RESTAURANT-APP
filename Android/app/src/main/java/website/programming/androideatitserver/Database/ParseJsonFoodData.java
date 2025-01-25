package website.programming.androideatitserver.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import website.programming.androideatitserver.Model.Food;

/**
 * Created by cokel on 3/25/2018.
 */

public class ParseJsonFoodData {

    String parse_json;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<Food> FoodArrayLists = new ArrayList<Food>();
    String Foodid, name, image, description, price, discount, menuid;
    int count=0;
    String jsondata="";

    public ParseJsonFoodData(String jsondata) {
        this.jsondata = jsondata;
    }

    public ArrayList<Food> getFoodData() {
        try {
            jsonObject = new JSONObject(jsondata);
            count=0;
            jsonArray=jsonObject.getJSONArray("server_response");
            //  Log.i("jsonarray:",String.valueOf(jsonArray.length()));
            while(count<jsonArray.length()){

                JSONObject jo = jsonArray.getJSONObject(count);

                Foodid = jo.getString("id");
                name = jo.getString("name");
                image = jo.getString("image");
                description = jo.getString("description");
                price = jo.getString("price");
                discount = jo.getString("discount");
                menuid = jo.getString("menuid");
                Food food = new Food(Foodid);
                FoodArrayLists.add(food);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return FoodArrayLists;
    }

    public int getFoodCount(){
        return count;
    }
}
