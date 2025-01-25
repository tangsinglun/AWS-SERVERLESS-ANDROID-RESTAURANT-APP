package website.programming.androideatitserver.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import website.programming.androideatitserver.Common.Common;

/**
 * Created by ashu on 16-Apr-16.
 */
public class DatabaseInsert extends AsyncTask<String,Void,String> {

    Context ctx;
    public DatabaseInsert(Context ctx){
        this.ctx=ctx;
    }


    protected String doInBackground(String... params) {

        String reg_url=Common.SERVER_URL + "add.php";
        String method= params[0];
        Log.i("URL", reg_url);

        if(method.equals("Category")){
            String categoryid= params[1];
            String name=params[2];
            String image=params[3];

            Log.i("Category", "Enter");

            try {
                URL url= new URL(reg_url);
                Log.i("URL:",reg_url );
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("method", "UTF-8")+"="+URLEncoder.encode(method,"UTF-8")+"&"+
                        URLEncoder.encode("categoryid", "UTF-8")+"="+URLEncoder.encode(categoryid,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(image,"UTF-8");

                Log.i("url:", data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                return "Category added Successfully.";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("CategoryUpdate")){
            String categoryid= params[1];
            String name=params[2];
            String image=params[3];

            Log.i("Category Update", "Enter");

            try {
                URL url= new URL(reg_url);
                Log.i("URL:",reg_url );
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("method", "UTF-8")+"="+URLEncoder.encode(method,"UTF-8")+"&"+
                        URLEncoder.encode("categoryid", "UTF-8")+"="+URLEncoder.encode(categoryid,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(image,"UTF-8");

                Log.i("url:", data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                return "Category Update Successfully.";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("DeleteCategory")){

            Log.i("Category DELETE", "Enter");

            String categoryid= params[1];

            try {
                URL url= new URL(reg_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("method", "UTF-8")+"="+URLEncoder.encode(method,"UTF-8")+"&"+
                        URLEncoder.encode("categoryid","UTF-8")+"="+URLEncoder.encode(categoryid,"UTF-8");
                Log.i("url:", data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                return "Delete category Successfully.";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("Food")){

            String foodid= params[1];
            String name=params[2];
            String image=params[3];
            String description=params[4];
            String price=params[5];
            String discount=params[6];
            String menuId=params[7];

            Log.i("Food", "Enter");

            try {
                URL url= new URL(reg_url);
                Log.i("URL:",reg_url );
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("method", "UTF-8")+"="+URLEncoder.encode(method,"UTF-8")+"&"+
                        URLEncoder.encode("foodid", "UTF-8")+"="+URLEncoder.encode(foodid,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(image,"UTF-8")+"&"+
                        URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description,"UTF-8")+"&"+
                        URLEncoder.encode("price","UTF-8")+"="+URLEncoder.encode(price,"UTF-8")+"&"+
                        URLEncoder.encode("discount","UTF-8")+"="+URLEncoder.encode(discount,"UTF-8")+"&"+
                        URLEncoder.encode("menuId","UTF-8")+"="+URLEncoder.encode(menuId,"UTF-8");

                Log.i("url:", data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                return "Food added Successfully.";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("DeleteFood")){

            Log.i("Food DELETE", "Enter");

            String foodid= params[1];

            try {
                URL url= new URL(reg_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("method", "UTF-8")+"="+URLEncoder.encode(method,"UTF-8")+"&"+
                        URLEncoder.encode("foodid","UTF-8")+"="+URLEncoder.encode(foodid,"UTF-8");
                Log.i("url:", data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                return "Delete Food Successfully.";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("FoodUpdate")){
            String foodid= params[1];
            String name=params[2];
            String image=params[3];
            String description=params[4];
            String price=params[5];
            String discount=params[6];
            String menuId=params[7];

            Log.i("Category Update", "Enter");

            try {
                URL url= new URL(reg_url);
                Log.i("URL:",reg_url );
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("method", "UTF-8")+"="+URLEncoder.encode(method,"UTF-8")+"&"+
                        URLEncoder.encode("foodid", "UTF-8")+"="+URLEncoder.encode(foodid,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(image,"UTF-8")+"&"+
                        URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description,"UTF-8")+"&"+
                        URLEncoder.encode("price","UTF-8")+"="+URLEncoder.encode(price,"UTF-8")+"&"+
                        URLEncoder.encode("discount","UTF-8")+"="+URLEncoder.encode(discount,"UTF-8")+"&"+
                        URLEncoder.encode("menuId","UTF-8")+"="+URLEncoder.encode(menuId,"UTF-8");

                Log.i("url:", data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                return "Food Update Successfully.";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("DeleteOrder")){

            Log.i("Order DELETE", "Enter");

            String Orderid = params[1];

            try {
                URL url= new URL(reg_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("method", "UTF-8")+"="+URLEncoder.encode(method,"UTF-8")+"&"+
                        URLEncoder.encode("Orderid","UTF-8")+"="+URLEncoder.encode(Orderid,"UTF-8");
                Log.i("url:", data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                return "Delete Order Successfully.";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("OrderStaus")){
            String status= params[1];
            String orderid= params[2];

            Log.i("Order Update", "Enter");

            try {
                URL url= new URL(reg_url);
                Log.i("URL:",reg_url );
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("method", "UTF-8")+"="+URLEncoder.encode(method,"UTF-8")+"&"+
                        URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8")+"&"+
                        URLEncoder.encode("orderid","UTF-8")+"="+URLEncoder.encode(orderid,"UTF-8");

                Log.i("url:", data);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                return "Order Update Successfully.";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
