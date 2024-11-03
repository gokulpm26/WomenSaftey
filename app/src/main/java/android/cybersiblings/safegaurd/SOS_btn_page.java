package android.cybersiblings.safegaurd;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import at.markushi.ui.CircleButton;

public class SOS_btn_page extends AppCompatActivity {
    private CircleButton sosButton;
    private String userInfo;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private LocationHelperClass locationHelper;
    private DatabaseHelperClass databaseHelperClass;
    private SharedPreferences sharedPreferences;
    private SmsManager smsManager;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_btn_page);

        sosButton = findViewById(R.id.sos_button);
        sharedPreferences = getSharedPreferences(VariableHolder.SafeGuard, MODE_PRIVATE);
        userInfo = sharedPreferences.getString(VariableHolder.Username, "");
        navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.log_out) {
                alert();
            } else if (id == R.id.profile) {
                Intent intent = new Intent(SOS_btn_page.this, My_profile.class);
                startActivity(intent);
                finish();
            }
            return false;
        });

        databaseHelperClass = new DatabaseHelperClass(SOS_btn_page.this);
        locationHelper = new LocationHelperClass(this);
        smsManager = SmsManager.getDefault();

        sosButton.setOnClickListener(v -> {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    startLiveLocationUpdates();
                } else {
                    promptEnableLocation();
                }
            } else {
                requestPermissions();
            }
        });
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS},
                PERMISSION_REQUEST_CODE);
    }

    private void startLiveLocationUpdates() {
        locationHelper.getLocation((latitude, longitude) -> {
            String panicMessage = "I am in Danger! I am trusting in you and sending my current live location: " +
                    "http://maps.google.com/?q=" + latitude + "," + longitude;
            String favorite1 = sharedPreferences.getString(VariableHolder.fav1, "");
            String favorite2 = sharedPreferences.getString(VariableHolder.fav2, "");
            Log.v("SOS", "fav1 " + favorite1 + " fav2 " + favorite2);
            sendSms(favorite1, panicMessage);
            sendSms(favorite2, panicMessage);
        });
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Message sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
            Log.d("SOS_btn_page", "Message sent to " + phoneNumber);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send message to " + phoneNumber, Toast.LENGTH_SHORT).show();
            Log.e("SOS_btn_page", "Failed to send message to " + phoneNumber, e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLiveLocationUpdates();
            } else {
                Toast.makeText(this, "Permission denied. Cannot send SMS or get location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void promptEnableLocation() {
        Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SOS_btn_page.this);
        builder.setMessage(VariableHolder.confirm_logout);
        builder.setTitle(VariableHolder.Alert);
        builder.setCancelable(false);
        builder.setPositiveButton(VariableHolder.yes, (dialog, which) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(VariableHolder.LoginStatus, false);
            editor.apply();
            dialog.dismiss();
            Intent intent = new Intent(SOS_btn_page.this, Login.class);
            startActivity(intent);
            finish();
            Toast.makeText(SOS_btn_page.this, VariableHolder.LogoutSuccessful, Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton(VariableHolder.no, (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
