package android.cybersiblings.safegaurd;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Sign_up extends AppCompatActivity {
    private EditText name,phone,email,date_of_birth,password,re_password,fav1,fav2;
    private Button signup;
    private SharedPreferences sharedPreferences;
    private boolean Check,getCheck;
    private String Name,Phone,E_mail,dob,Password,Re_password;
    private long backPressedTime = 0;
    DatabaseHelperClass databaseHelperClass;
    private static final int REQUEST_SMS_PERMISSION = 123;
    private static final int REQUEST_LOCATION_PERMISSION = 124;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        name=(EditText) findViewById(R.id.name);
        phone=(EditText) findViewById(R.id.phone);
        email=(EditText)findViewById(R.id.email);
        date_of_birth =(EditText) findViewById(R.id.dob);
        fav1=findViewById(R.id.fav_contact_1);
        fav2=findViewById(R.id.fav_contact_2);
        password=(EditText) findViewById(R.id.password);
        re_password=(EditText) findViewById(R.id.re_password);
        signup=(Button)findViewById(R.id.signup);

        sharedPreferences=getSharedPreferences(VariableHolder.SafeGuard,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        databaseHelperClass=new DatabaseHelperClass(Sign_up.this);

        signup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Name=name.getText().toString().trim();
                Phone=phone.getText().toString().trim();
                E_mail=email.getText().toString().trim();
                dob=date_of_birth.getText().toString().trim();
                Password=password.getText().toString().trim();
                Re_password=re_password.getText().toString().trim();
                String Fav1=fav1.getText().toString().trim();
                String Fav2=fav2.getText().toString().trim();
                Check=Util.validate(Name,Phone,E_mail,dob,Password,Re_password,Fav1,Fav2,name,phone,email,date_of_birth,password,re_password,fav1,fav2);
                getCheck=readData(Phone);
                if (Check){
                    if(getCheck){
                        AlertDialog.Builder builder=new AlertDialog.Builder(Sign_up.this);
                        builder.setTitle(VariableHolder.Warning);
                        builder.setMessage(VariableHolder.user_exists);
                        builder.setCancelable(false);
                        builder.setPositiveButton(VariableHolder.login, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Sign_up.this,Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.create().show();
                    }else{
                        requestPermissions();
                        Toast.makeText(Sign_up.this, "Sign-up Successful", Toast.LENGTH_LONG).show();
                        editor.putString(VariableHolder.fav1,Fav1);
                        editor.putString(VariableHolder.fav2,Fav2);
                        editor.apply();
                        Log.v("signup","fav1 "+sharedPreferences.getString(VariableHolder.fav1,"") +" fav2 "+sharedPreferences.getString(VariableHolder.fav2,""));
                        databaseHelperClass.addDataToDatabase(Name,Util.encodeData(Phone),Util.encodeData(E_mail),dob,Util.encodeData(Password),Util.encodeData(Fav1),Util.encodeData(Fav2));
                        Intent intent=new Intent(Sign_up.this,Login.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }

        });
        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.datePicker(Sign_up.this,date_of_birth);
            }
        });

    }

    private boolean readData(String phone) {
        DatabaseHelperClass databaseHelperClass=new DatabaseHelperClass(getApplicationContext());
        Cursor cursor=databaseHelperClass.readData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, VariableHolder.Database_doesnt_contain_any_data, Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                if (Util.decodeData(cursor.getString(1)).matches(phone))
                    return true;
            }
        }
        return false;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_SMS_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thank You", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == REQUEST_LOCATION_PERMISSION && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Thank You", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Kindly grant access", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            Intent intent= new Intent(Sign_up.this, Login.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Press back again to exit the activity.", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();

    }

}