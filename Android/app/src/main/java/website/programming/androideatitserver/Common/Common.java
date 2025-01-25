package website.programming.androideatitserver.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import website.programming.androideatitserver.Model.Request;
import website.programming.androideatitserver.Model.User;
import website.programming.androideatitserver.Remote.APIService;
import website.programming.androideatitserver.Remote.FCMRetrofitClient;
import website.programming.androideatitserver.Remote.IGeoCoordinates;
import website.programming.androideatitserver.Remote.RetrofitClient;

/**
 * Created by cokel on 2/27/2018.
 */

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    //public static final String SERVER_URL = "http://wineofthenight.000webhostapp.com/EatIt/";
    //public static final String SERVER_URL = "http://programming.website/EatIt/";
    public static final String SERVER_URL = "http://192.168.86.26/EatIt/";
    public static final String AWS_SERVER_API = "https://hbdags2c3g.execute-api.us-east-2.amazonaws.com/dev";

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String DETAIL = "Detail";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static final String baseUrl = "https://maps.googleapis.com";

    public static final String fcmUrl = "https://fcm.googleapis.com/";

    public static String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }

    public static APIService getFCMService(){
        return FCMRetrofitClient.getClient(fcmUrl).create(APIService.class);
    }

    public static IGeoCoordinates getGeoCodeService(){
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight)
    {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);

        float scaleX = newWidth/(float)bitmap.getWidth();
        float scaleY = newHeight/(float)bitmap.getHeight();
        float pivotX=0, pivotY=0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null) {
                for (int i = 0; i < info.length; i++)
                {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }

        }
        return false;
    }
}
