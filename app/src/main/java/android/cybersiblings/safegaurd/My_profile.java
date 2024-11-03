package android.cybersiblings.safegaurd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class My_profile extends AppCompatActivity {
    private EditText name, phone, email, date_of_birth, password, re_password, fav1,fav2;
    private Button update;
    private String userId;
    private long backPressedTime = 0;
    SharedPreferences sharedPreferences;
    DatabaseHelperClass databaseHelperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        EdgeToEdge.enable(this);

        name = findViewById(R.id.up_name);
        phone = findViewById(R.id.up_phone);
        email = findViewById(R.id.up_email);
        date_of_birth = findViewById(R.id.up_dob);
        fav1=findViewById(R.id.up_fav_contact_1);
        fav2=findViewById(R.id.up_fav_contact_2);
        password = findViewById(R.id.up_password);
        re_password = findViewById(R.id.up_re_password);
        update = findViewById(R.id.update);

        databaseHelperClass = new DatabaseHelperClass(this);

        sharedPreferences=getSharedPreferences(VariableHolder.SafeGuard,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        userId = sharedPreferences.getString(VariableHolder.Username, "");

        displayUserData();

        update.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String Name = name.getText().toString().trim();
                String Phone = phone.getText().toString().trim();
                String E_mail = email.getText().toString().trim();
                String dob = date_of_birth.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Re_password = re_password.getText().toString().trim();
                String Fav1=fav1.getText().toString().trim();
                String Fav2=fav2.getText().toString().trim();


                if (Util.validate(Name, Phone, E_mail, dob, Password, Re_password,Fav1,Fav2, name, phone, email, date_of_birth, password, re_password,fav1,fav2)) {

                    editor.putString(VariableHolder.fav1,Fav1);
                    editor.putString(VariableHolder.fav2,Fav2);
                    editor.apply();
                    Log.v("my_profile","fav1 "+sharedPreferences.getString(VariableHolder.fav1,"") +" fav2 "+sharedPreferences.getString(VariableHolder.fav2,""));
                    databaseHelperClass.updateUserData(Name, Util.encodeData(Phone), Util.encodeData(E_mail), dob,Util.encodeData(Password),Util.encodeData(Fav1),Util.encodeData(Fav2));
                    Log.v("My_profile", "phone " + Phone);


                    Intent intent = new Intent(My_profile.this, SOS_btn_page.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void displayUserData() {
        Cursor cursor = databaseHelperClass.readData();
        if (cursor == null) {
            Log.e("My_profile", "Cursor is null");
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String phoneValue = Util.decodeData(cursor.getString(1));
                if (phoneValue != null && phoneValue.matches(userId)) {
                    name.setText(cursor.getString(0));
                    phone.setText(phoneValue);
                    email.setText(Util.decodeData(cursor.getString(2)));
                    date_of_birth.setText(cursor.getString(3));
                    password.setText(Util.decodeData(cursor.getString(4)));
                    re_password.setText(Util.decodeData(cursor.getString(4)));
                    fav1.setText(Util.decodeData(cursor.getString(5)));
                    fav2.setText(Util.decodeData(cursor.getString(6)));

                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            Intent intent= new Intent(My_profile.this, SOS_btn_page.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Press back again to exit the activity.", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();

    }
}
