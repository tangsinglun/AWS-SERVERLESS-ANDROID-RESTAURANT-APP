package website.programming.androideatitserver.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Model.Token;

/**
 * Created by cokel on 3/17/2018.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    public MyFirebaseIdService() {
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if(Common.currentUser != null)
             updateToServer(refreshedToken);
    }

    private void updateToServer(String refreshedToken) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token token = new Token(refreshedToken,true);//false because this token send from client app
        tokens.child(Common.currentUser.getPhone()).setValue(token);
    }
}
