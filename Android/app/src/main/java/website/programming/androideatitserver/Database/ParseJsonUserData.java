package website.programming.androideatitserver.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import website.programming.androideatitserver.Model.User;

/**
 * Created by cokel on 3/25/2018.
 */

public class ParseJsonUserData {

    String parse_json;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<User> UserArrayLists = new ArrayList<User>();
    String name, phone,  securecode, password;
    int count=0;
    String jsondata;

    public ParseJsonUserData(String jsondata) {
        this.jsondata = jsondata;
    }

    public List<User> getUserData() {
        try {
            jsonObject = new JSONObject(this.jsondata);
            count=0;
            jsonArray = new JSONArray(jsonObject.get("items").toString());
            while(count<jsonArray.length()){
                JSONObject jo = jsonArray.getJSONObject(count);
                User user = new User();
                user.setUserId(jo.getString("userId"));
                user.setIsStaff(true);
                user.setName(name = jo.getString("name"));
                user.setPassword(jo.getString("password"));
                user.setSecureCode(jo.getString("securecode"));
                user.setPhone(jo.getString("phone"));
                UserArrayLists.add(user);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return UserArrayLists;
    }

    public int getUserCount(){
        return count;
    }
}
