package website.programming.androideatitserver.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Model.Category;
import website.programming.androideatitserver.Model.User;

/**
 * Created by ashu on 16-Apr-16.
 */
public class PostServices extends AsyncTask<Object,Void,String> {

    Context ctx;

    public PostServices(Context ctx){
        this.ctx=ctx;
    }


    private String passwordHash(String password){
        // Hash the password
        int flags = Base64.NO_WRAP | Base64.URL_SAFE;
        return Base64.encodeToString(password.getBytes(), flags);
    }


    protected String doInBackground(Object... object) {

        String reg_url=Common.SERVER_URL + "add.php";
        Log.i("URL", reg_url);
        String method = ((String) object[0]);

        if(method.equals("signin")){

            JSONObject userObject = new JSONObject();
            String jwt = null;
            User currentUser = ((User) object[1]);

            try {
                userObject.put("phone", currentUser.getPhone());
                userObject.put("password_hash", passwordHash(currentUser.getPassword()));
                userObject.put("name", currentUser.getName());
                userObject.put("isStaff", currentUser.getIsStaff());
                userObject.put("secureCode", currentUser.getSecureCode());
                // We need a signing key, so we'll create one just for this example. Usually
                // the key would be read from your application configuration instead.
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

                jwt = Jwts.builder().setSubject(userObject.toString()).signWith(key).compact();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            String json_url = Common.AWS_SERVER_API + "/users";

            try {
                URL url= new URL(json_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                String barerAuth = "Bearer " + jwt;
                httpURLConnection.setRequestProperty ("Authorization", barerAuth);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

//                String data= URLEncoder.encode("phone", "UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+"&"+
//                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");

                JSONObject data = userObject;

                //   Log.i("url:", data);

                bufferedWriter.write(String.valueOf(data));
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream IS= httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(IS)));
                String json_string=bufferedReader.readLine();
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return json_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("signup")){

            JSONObject userObject = new JSONObject();
            String jwt = null;
            User currentUser = ((User) object[1]);

            try {
                userObject.put("userId", currentUser.getUserId());
                userObject.put("phone", currentUser.getPhone());
                userObject.put("password_hash", passwordHash(currentUser.getPassword()));
                userObject.put("name", currentUser.getName());
                userObject.put("isStaff", currentUser.getIsStaff());
                userObject.put("secureCode", currentUser.getSecureCode());
                // We need a signing key, so we'll create one just for this example. Usually
                // the key would be read from your application configuration instead.
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

                jwt = Jwts.builder().setSubject(userObject.toString()).signWith(key).compact();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            String json_url = Common.AWS_SERVER_API + "/createuser";

            try {
                URL url= new URL(json_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                String barerAuth = "Bearer " + jwt;
                httpURLConnection.setRequestProperty ("Authorization", barerAuth);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

//                String data= URLEncoder.encode("phone", "UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+"&"+
//                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");

                JSONObject data = userObject;

                //   Log.i("url:", data);

                bufferedWriter.write(String.valueOf(data));
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream IS= httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(IS)));
                String json_string=bufferedReader.readLine();
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return json_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("DisplayCategory")){
            JSONObject dataObject = new JSONObject();
            JSONObject userObject = new JSONObject();
            String jwt = null;
            User currentUser = ((User) object[1]);
            String foodId = ((String) object[2]);

            try {
                userObject.put("phone", currentUser.getPhone());
                userObject.put("password_hash", passwordHash(currentUser.getPassword()));
                userObject.put("name", currentUser.getName());
                userObject.put("isStaff", currentUser.getIsStaff());
                userObject.put("secureCode", currentUser.getSecureCode());
                // We need a signing key, so we'll create one just for this example. Usually
                // the key would be read from your application configuration instead.
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

                jwt = Jwts.builder().setSubject(userObject.toString()).signWith(key).compact();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            String json_url = Common.AWS_SERVER_API + "/getfoods/" + foodId;

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                String barerAuth = "Bearer " + jwt;
                httpURLConnection.setRequestProperty ("Authorization", barerAuth);
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String json_string=bufferedReader.readLine();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return json_string;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("GetMaximumID")){
            JSONObject userObject = new JSONObject();
            String jwt = null;
            User currentUser = ((User) object[1]);

            try {
                userObject.put("phone", currentUser.getPhone());
                userObject.put("password_hash", passwordHash(currentUser.getPassword()));
                userObject.put("name", currentUser.getName());
                userObject.put("isStaff", currentUser.getIsStaff());
                userObject.put("secureCode", currentUser.getSecureCode());
                // We need a signing key, so we'll create one just for this example. Usually
                // the key would be read from your application configuration instead.
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

                jwt = Jwts.builder().setSubject(userObject.toString()).signWith(key).compact();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            String json_url = Common.AWS_SERVER_API + "/getmaxid";

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                String barerAuth = "Bearer " + jwt;
                httpURLConnection.setRequestProperty ("Authorization", barerAuth);
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String json_string=bufferedReader.readLine();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return json_string;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("DeleteCategory")){
            JSONObject dataObject = new JSONObject();
            JSONObject userObject = new JSONObject();
            String jwt = null;
            User currentUser = ((User) object[1]);
            String foodId = ((String) object[2]);

            try {
                userObject.put("phone", currentUser.getPhone());
                userObject.put("password_hash", passwordHash(currentUser.getPassword()));
                userObject.put("name", currentUser.getName());
                userObject.put("isStaff", currentUser.getIsStaff());
                userObject.put("secureCode", currentUser.getSecureCode());
                // We need a signing key, so we'll create one just for this example. Usually
                // the key would be read from your application configuration instead.
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

                jwt = Jwts.builder().setSubject(userObject.toString()).signWith(key).compact();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            String json_url = Common.AWS_SERVER_API + "/deletefood/" + foodId;

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                String barerAuth = "Bearer " + jwt;
                httpURLConnection.setRequestProperty ("Authorization", barerAuth);
                httpURLConnection.setRequestMethod("POST");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String json_string=bufferedReader.readLine();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return json_string;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("Category")){
            JSONObject dataObject = new JSONObject();
            JSONObject userObject = new JSONObject();
            String jwt = null;
            String json_url = "";
            User currentUser = ((User) object[1]);
            Category category = ((Category) object[2]);

            try {
                userObject.put("phone", currentUser.getPhone());
                userObject.put("password_hash", passwordHash(currentUser.getPassword()));
                userObject.put("name", currentUser.getName());
                userObject.put("isStaff", currentUser.getIsStaff());
                userObject.put("secureCode", currentUser.getSecureCode());
                // We need a signing key, so we'll create one just for this example. Usually
                // the key would be read from your application configuration instead.
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

                jwt = Jwts.builder().setSubject(userObject.toString()).signWith(key).compact();

                if (object[3] == "Create") {
                    json_url = Common.AWS_SERVER_API + "/createfood/" + category.getImage();
                }
                else {
                    json_url = Common.AWS_SERVER_API + "/editfood/" + category.getImage();
                }

                dataObject.put("id", category.getCategoryid());
                dataObject.put("name", category.getName());
                dataObject.put("image", category.getImage());
                dataObject.put("description", category.getDescription());
                dataObject.put("price", category.getPrice());
                dataObject.put("discount", category.getDiscount());
                dataObject.put("menuid", category.getMenuid());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                URL url= new URL(json_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                String barerAuth = "Bearer " + jwt;
                httpURLConnection.setRequestProperty ("Authorization", barerAuth);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os= httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

//                String data= URLEncoder.encode("phone", "UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+"&"+
//                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");

                JSONObject data = dataObject;

                //   Log.i("url:", data);

                bufferedWriter.write(String.valueOf(data));
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream IS= httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(IS)));
                String json_string=bufferedReader.readLine();
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return json_string;

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
