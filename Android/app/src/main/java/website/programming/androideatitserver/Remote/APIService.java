package website.programming.androideatitserver.Remote;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import website.programming.androideatitserver.Model.MyResponse;
import website.programming.androideatitserver.Model.Sender;

/**
 * Created by cokel on 3/17/2018.
 */

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAkkbl5Ik:APA91bE4CTwBrGPiEOvI-_EkfAcSOk5jLbooF09W8XCZHLr6m4XPjCIJh4zPU1KzCOVwWTm92IC_2NEuSTqsxK0MW8hzlBuvz2yrtqHZGLgIpi0TS0r-jah9PzkPKAgfD9kJXk8kVt_k"

            }


    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
