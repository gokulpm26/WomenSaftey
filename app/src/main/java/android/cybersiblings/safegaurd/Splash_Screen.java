package android.cybersiblings.safegaurd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    boolean isLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences=getSharedPreferences(VariableHolder.SafeGuard,MODE_PRIVATE);
        isLogged=sharedPreferences.getBoolean(VariableHolder.LoginStatus,false);
        Thread thread= new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    if (isLogged) {
                        Intent intent = new Intent(Splash_Screen.this, SOS_btn_page.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(Splash_Screen.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }

        };
        thread.start();

    }
}