package website.programming.androideatitserver.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import website.programming.androideatitserver.Common.Common;

public class DatabaseSelectServices extends AsyncTask<String,Void,String>{

    String json_string= "Nodata";
    public static String parse_json;
    String method;
    String json_url;
    Context ctx;
    String BufferNoData = "Nodata";

    public DatabaseSelectServices() {
    }

    protected String doInBackground(String... params) {
        String method= params[0];

       // Log.i("Sign In method", method);
        if(method.matches("signup")) {
            String searchCriteria = params[1];
            json_url = Common.SERVER_URL + "getuserjson.php?method="+method+"&phone=" + searchCriteria;
           // Log.i("Json Link: ", json_url);
        }
        else if(method.matches("signin")) {
            String searchCriteria = params[1];
            String searchCriteria2 = params[2];
            json_url = Common.SERVER_URL + "getuserjson.php?method="+method+"&phone="+ searchCriteria + "&password=" + searchCriteria2;
           // Log.i("Json Link: ", json_url);
        }
        else if(method.matches("forgetpwd")) {
            String searchCriteria = params[1];
            String searchCriteria2 = params[2];
            json_url = Common.SERVER_URL + "getuserjson.php?method="+method+"&phone="+ searchCriteria + "&securecode=" + searchCriteria2;
            //Log.i("Json Link: ", json_url);
        }
        else if(method.matches("Category")) {
            json_url = Common.SERVER_URL + "getcategoryjson.php?method="+method;
            //json_url = Common.SERVER_URL + "Category.php";
           // Log.i("Json Link: ", json_url);
        }
        else if(method.matches("AllFood")) {
            //String searchCriteria = params[1];
            json_url = Common.SERVER_URL + "getfoodjson.php?method="+method;
            //json_url = Common.SERVER_URL + "Food.php";
            //   Log.i("Json Link: ", json_url);
        }
        else if(method.matches("Food")) {
            String searchCriteria = params[1];
            json_url = Common.SERVER_URL + "getfoodjson.php?method="+method+"&categoryid=" + searchCriteria;
            //json_url = Common.SERVER_URL + "Food.php";
         //   Log.i("Json Link: ", json_url);
        }
        else if(method.matches("FoodDetail")) {
            String searchCriteria = params[1];
            json_url = Common.SERVER_URL + "getfoodjson.php?method="+method+"&id=" + searchCriteria;
            //json_url = Common.SERVER_URL + "Food.php";
            //   Log.i("Json Link: ", json_url);
        }
        else if(method.matches("FoodItem")) {
            String searchCriteria = params[1];
            json_url = Common.SERVER_URL + "getfoodjson.php?method="+method+"&name=" + searchCriteria;
            //json_url = Common.SERVER_URL + "Food.php";
            Log.i("Json Link: ", json_url);
        }
        else if(method.matches("Rating")) {
            String searchCriteria = params[1];
            json_url = Common.SERVER_URL + "getratingjson.php?method="+method+"&foodid=" + searchCriteria;
            //json_url = Common.SERVER_URL + "Food.php";
            //Log.i("Json Link: ", json_url);
        }
        else if(method.matches("AllOrderStatus")) {
            Log.i("All Order Status: ", "Entered");
           // String searchCriteria = params[1];
            json_url = Common.SERVER_URL + "getorderjson.php?method="+method;
            //json_url = Common.SERVER_URL + "Food.php";
            Log.i("Json Link: ", json_url);
        }
        else if(method.matches("AllOrderDetail")) {
            Log.i("All Order Detail: ", "Entered");
            String searchCriteria = params[1];
            json_url = Common.SERVER_URL + "getorderDetailjson.php?method="+method+"&order_id=" + searchCriteria;
            //json_url = Common.SERVER_URL + "Food.php";
            Log.i("Json Link: ", json_url);
        }

        try {
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            StringBuilder stringBuilder = new StringBuilder();
            json_string=bufferedReader.readLine().replaceAll("/\"", "\"");
            int Count=0;
            while(!(json_string.matches(BufferNoData))){
                stringBuilder.append(json_string+"\n");
                json_string=bufferedReader.readLine().replaceAll("/\"", "\"");
                Count++;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            if(Count<1){
                return BufferNoData;
            }
            else {
                return stringBuilder.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        parse_json = s;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

