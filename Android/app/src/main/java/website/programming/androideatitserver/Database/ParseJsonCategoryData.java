package website.programming.androideatitserver.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import website.programming.androideatitserver.Model.Category;

/**
 * Created by cokel on 3/25/2018.
 */

public class ParseJsonCategoryData {

    String parse_json;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<Category> CategoryArrayLists = new ArrayList<Category>();
    String Categoryid, name, image, menuid;
    int count=0;
    String jsondata;

    public ParseJsonCategoryData(String jsondata) {
        this.jsondata = jsondata;
    }

    public ArrayList<Category> getCategoryData() {
        try {
            jsonObject = new JSONObject(this.jsondata);
            count=0;
            jsonArray = new JSONArray(jsonObject.get("items").toString());
            while(count<jsonArray.length()){
                JSONObject jo = jsonArray.getJSONObject(count);
                Category category = new Category(jo.getString("id"));
                category.setName(jo.getString("name"));
                category.setImage(jo.getString("image"));
                category.setMenuid(jo.getString("menuid"));
                category.setDescription(jo.getString("description"));
                category.setPrice(Integer.parseInt(jo.getString("price")));
                category.setDiscount(Integer.parseInt(jo.getString("discount")));
                CategoryArrayLists.add(category);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CategoryArrayLists;
    }

    public int getCategoryCount(){
        return count;
    }
}
