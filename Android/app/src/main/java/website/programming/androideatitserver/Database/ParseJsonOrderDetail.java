package website.programming.androideatitserver.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import website.programming.androideatitserver.Model.Food;
import website.programming.androideatitserver.Model.Order;

/**
 * Created by cokel on 3/25/2018.
 */

public class ParseJsonOrderDetail {

    String parse_json;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<Order> OrderDetailArrayLists = new ArrayList<Order>();
    String productId, productName, quantity, price, discount;
    int count=0, foodcount = 0;
    String jsondata="",id="";

    public ParseJsonOrderDetail(String jsondata,String order_id) {
        this.id = order_id;
        this.jsondata = jsondata;
    }

    public ArrayList<Order> getOrderDetailData() {
        try {
            jsonObject = new JSONObject(jsondata);
            count=0;
            foodcount=0;
            jsonArray=jsonObject.getJSONArray("server_response");
            //  Log.i("jsonarray:",String.valueOf(jsonArray.length()));
            while(count<jsonArray.length()){

                JSONObject jo = jsonArray.getJSONObject(count);
                JSONArray jsonfoodArray=jo.getJSONArray("foods");

                while(foodcount<jsonfoodArray.length()) {
                    JSONObject jfo = jsonfoodArray.getJSONObject(foodcount);

                    productId = jfo.getString("productId");
                    productName = jfo.getString("productName");
                    quantity = jfo.getString("quantity");
                    price = jfo.getString("price");
                    discount = jfo.getString("discount");
                    Order Orderdetail = new Order(productId, productName, quantity, price, discount);
                    OrderDetailArrayLists.add(Orderdetail);
                    foodcount++;
                }
                count++;
                foodcount=0;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return OrderDetailArrayLists;
    }

    public int getFoodCount(){
        return count;
    }
}
