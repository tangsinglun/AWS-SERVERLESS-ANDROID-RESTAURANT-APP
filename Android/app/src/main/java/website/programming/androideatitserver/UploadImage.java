package website.programming.androideatitserver;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;
import java.util.concurrent.ExecutionException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Model.User;

/**
 * Created by cokel on 3/30/2018.
 */

public class UploadImage extends AsyncTask<Object,Void,String> {

    Bitmap image;
    String name;

    public UploadImage() {
    }

    @Override
    protected void onPostExecute(String result) {
        //return result;
    }

    private String passwordHash(String password){
        // Hash the password
        int flags = Base64.NO_WRAP | Base64.URL_SAFE;
        return Base64.encodeToString(password.getBytes(), flags);
    }

    @Override
    protected String doInBackground(Object... object) {


        try {
            String encodedImage = ((String) object[0]);
            String imageName = ((String) object[1]);

            String json_url = Common.AWS_SERVER_API + "/food/" + imageName;

            User user = ((User) object[2]);

            JSONObject userObject = new JSONObject();
            JSONObject fileObject = new JSONObject();
            JSONObject fileDataObject = new JSONObject();
            String jwt = null;

            try {
                userObject.put("phone", user.getPhone());
                userObject.put("password_hash", passwordHash(user.getPassword()));
                userObject.put("name", user.getName());
                userObject.put("isStaff", user.getIsStaff());
                userObject.put("secureCode", user.getSecureCode());

                fileDataObject.put("data", encodedImage);
                fileObject.put("id", imageName);
                fileObject.put("file", fileDataObject);

                // We need a signing key, so we'll create one just for this example. Usually
                // the key would be read from your application configuration instead.
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

                jwt = Jwts.builder().setSubject(userObject.toString()).signWith(key).compact();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            URL url= new URL(json_url);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            String barerAuth = "Bearer " + jwt;
            httpURLConnection.setRequestProperty ("Authorization", barerAuth);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os= httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

//            String data= URLEncoder.encode("method", "UTF-8")+"="+URLEncoder.encode(method,"UTF-8")+"&"+
//                    URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
//                    URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(encodedImage,"UTF-8");

            String data= URLEncoder.encode(encodedImage,"UTF-8");

            //   Log.i("url:", data);

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();


            os.close();
            InputStream IS= httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(IS)));
            String json_string=bufferedReader.readLine();
            bufferedReader.close();
            IS.close();
            return json_string;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
