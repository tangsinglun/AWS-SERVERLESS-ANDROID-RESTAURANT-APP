package website.programming.androideatitserver.Database;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import website.programming.androideatitserver.Model.Request;

/**
 * Created by cokel on 4/1/2018.
 */
public class ParseJsonOrderData {

    String parse_json;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<Request> RequestArrayLists = new ArrayList<Request>();
    String Requestid, phone,name, address,total, status, Comment,paymentState;
    int count=0;
    String jsondata="";
    Context context;

    public ParseJsonOrderData(String jsondata, Context context) {
        this.jsondata = jsondata;
        this.context = context;
    }

    public ArrayList<Request> getRequestData() {
        try {
            jsonObject = new JSONObject(jsondata);
            count=0;
            jsonArray=jsonObject.getJSONArray("server_response");
            //  Log.i("jsonarray:",String.valueOf(jsonArray.length()));
            while(count<jsonArray.length()){

                JSONObject jo = jsonArray.getJSONObject(count);

                Requestid = jo.getString("order_id");
                phone = jo.getString("phone");
                name = jo.getString("name");
                address = jo.getString("address");
                total = jo.getString("total");
                status = jo.getString("status");
                Comment = jo.getString("Comment");
                paymentState = jo.getString("paymentState");



                Request request = new Request(context,Requestid, phone, name, address, total,status,Comment,paymentState);
                RequestArrayLists.add(request);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RequestArrayLists;
    }

    public int getRequestCount(){
        return count;
    }
}

