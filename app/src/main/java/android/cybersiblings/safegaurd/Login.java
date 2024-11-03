package android.cybersiblings.safegaurd;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    private EditText UserInfo,Password;
    private TextView signup;
    private Button login;
    private Boolean Check;
    private long backPressedTime = 0;
    DatabaseHelperClass databaseHelperClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        UserInfo=(EditText) findViewById(R.id.login_user);
        Password=(EditText) findViewById(R.id.login_password);
        login=(Button) findViewById(R.id.log_in);
        signup=(TextView) findViewById(R.id.next);


        databaseHelperClass=new DatabaseHelperClass(getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences(VariableHolder.SafeGuard, MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Sign_up.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String userInfo,password;
                userInfo=UserInfo.getText().toString().trim();
                password=Password.getText().toString().trim();
                Check=Util.isVerified(userInfo,password,UserInfo,Password);
                if (Check){
                    if (databaseHelperClass.verifyUser(userInfo,password).getLogin_status()){
                        Intent intent=new Intent(getApplicationContext(),SOS_btn_page.class);
                        startActivity(intent);
                        editor.putString(VariableHolder.Username,userInfo);
                        editor.putBoolean(VariableHolder.LoginStatus,true);
                        editor.apply();
                        finish();
                    }else{
                        Util.alert(databaseHelperClass.verifyUser(userInfo,password).getLogin_message(),Login.this);
                    }
                }
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            alertBackPressed();
        } else {
            Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();

    }
    void alertBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Are you sure you want to exit this app?");
        builder.setTitle(VariableHolder.Warning);
        builder.setCancelable(false);
        builder.setPositiveButton(VariableHolder.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }

        });
        builder.setNegativeButton(VariableHolder.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
        });
        builder.create().show();
    }
}