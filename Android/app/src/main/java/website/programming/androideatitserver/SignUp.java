package website.programming.androideatitserver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Database.DatabaseSelectServices;
import website.programming.androideatitserver.Database.DatabaseInsert;
import website.programming.androideatitserver.Database.ParseJsonUserData;
import website.programming.androideatitserver.Database.PostServices;
import website.programming.androideatitserver.Model.User;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtPhone, edtName, edtPassword, edtSecureCode;
    String name,phone,securecode,password;
    Button btnSignUp, btnBack;
    String method;
    String jsonUserData = null;
    String BufferNoData = "Nodata";
    List<User> userData;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.activity_sign_up);

        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtSecureCode = (MaterialEditText) findViewById(R.id.edtSecureCode);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnBack = (Button) findViewById(R.id.btnBack);



        if(!Common.isConnectedToInternet(SignUp.this)) {
            Toast.makeText(SignUp.this,"Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    public void signup(View view){
        final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        if (Common.isConnectedToInternet(getBaseContext())) {
            method= "signup";
            try {
                User user = new User();
                String userId = UUID.randomUUID().toString();
                user.setUserId(userId);
                user.setPhone(edtPhone.getText().toString());
                user.setPassword(edtPassword.getText().toString());
                user.setName(edtName.getText().toString());
                user.setSecureCode(edtSecureCode.getText().toString());
                user.setIsStaff(true);
                method= "signup";
                PostServices ps= new PostServices(SignUp.this);
                jsonUserData =  ps.execute(method, user).get();
                //parse_json = databaseSelectServices.parse_json;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            if(!TextUtils.isEmpty(jsonUserData)){
                Log.i("JsonData", jsonUserData);
                ParseJsonUserData ParseJsonUserData= new ParseJsonUserData(jsonUserData);
                userData = ParseJsonUserData.getUserData();
                Toast.makeText(SignUp.this, "Sign Up Successfully.", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                //Toast.makeText(SignUp.this, "Sign Up Successfully.", Toast.LENGTH_SHORT).show();
            }
            else {
                mDialog.dismiss();
                Toast.makeText(SignUp.this, "Phone Number already Registered.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(SignUp.this, "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            return;
        }

    }

}
