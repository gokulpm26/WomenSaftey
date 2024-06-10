package android.cybersiblings.safegaurd;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Sign_up extends AppCompatActivity {
private EditText name,phone,email,date_of_birth,emergency_contact1,emergency_contact2;
private Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        name=(EditText) findViewById(R.id.name);
        phone=(EditText) findViewById(R.id.phone);
        email=(EditText) findViewById(R.id.email);
        date_of_birth =(EditText) findViewById(R.id.dob);
        emergency_contact1=(EditText) findViewById(R.id.ephone1);
        emergency_contact2=(EditText) findViewById(R.id.ephone2);

        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c= Calendar.getInstance();

                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(Sign_up.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date_of_birth.setText(dayOfMonth+"/"+(month+01)+"/"+year);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }
}