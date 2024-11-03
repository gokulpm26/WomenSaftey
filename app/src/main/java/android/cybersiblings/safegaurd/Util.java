package android.cybersiblings.safegaurd;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import java.util.Base64;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static void datePicker(Context context, EditText date_of_birth) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date_of_birth.setText(dayOfMonth + "/" + (month + 01) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void errorSetterAndFocus(EditText editText, String message) {
        editText.setError(message);
        editText.setFocusedByDefault(true);
        editText.requestFocus();
    }

    public static boolean validatePhone(String phone) {
        String string = "^[6-9][0-9]{9}$";
        Pattern pattern = Pattern.compile(string);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean validateEmail(String email) {
        String string = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(string);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePassword(String password) {
        String string = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(string);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean validate(String Name, String Phone, String Email, String Dob, String Password, String Re_password,String fav1,String fav2, EditText name, EditText phone, EditText email, EditText date_of_birth, EditText password, EditText re_password,EditText FAV1,EditText FAV2) {
        if (Name.isEmpty()) {
            errorSetterAndFocus(name, VariableHolder.Enter_name);
            return false;
        }
        if (Phone.isEmpty()) {
            errorSetterAndFocus(phone, VariableHolder.Enter_Phone_Number);
            return false;
        }
        if (!validatePhone(Phone)) {
            errorSetterAndFocus(phone, VariableHolder.Invalid_Phone_Number);
            return false;
        }
        if (Email.isEmpty()) {
            errorSetterAndFocus(email, VariableHolder.Enter_email);
            return false;
        }
        if (!validateEmail(Email)) {
            errorSetterAndFocus(email, VariableHolder.Invalid_email);
            return false;
        }
        if (Dob.isEmpty()) {
            errorSetterAndFocus(date_of_birth, VariableHolder.Date_of_Birth);
            return false;
        }
        if(fav1.isEmpty()){
            errorSetterAndFocus(FAV1,VariableHolder.Enter_Phone_Number);
            return false;
        }
        if (!fav1.isEmpty()){
            if (!validatePhone(fav1)){
                errorSetterAndFocus(FAV1,VariableHolder.Invalid_Phone_Number_Format);
                return false;
            }
        }
        if(fav2.isEmpty()){
            errorSetterAndFocus(FAV2,VariableHolder.Enter_Phone_Number);
            return false;
        }
        if (!fav2.isEmpty()){
            if (!validatePhone(fav2)){
                errorSetterAndFocus(FAV2,VariableHolder.Invalid_Phone_Number_Format);
                return false;
            }
        }
        if (Password.isEmpty()) {
            errorSetterAndFocus(password, VariableHolder.password_empty);
            return false;
        }
        if (Re_password.isEmpty()) {
            errorSetterAndFocus(re_password, VariableHolder.password__empty);
            return false;
        }
        if (Password.length() < 8) {
            errorSetterAndFocus(password, VariableHolder.password_length);
            return false;
        }
        if (!validatePassword(Password)) {
            errorSetterAndFocus(password, VariableHolder.password_criteria);
            return false;
        }
        if (Re_password.length() < 8) {
            errorSetterAndFocus(re_password, VariableHolder.password_length);
            return false;
        }
        if (!validatePassword(Re_password)) {
            errorSetterAndFocus(re_password, VariableHolder.password_criteria);
            return false;
        }
        if (!Password.matches(Re_password)) {
            errorSetterAndFocus(re_password, VariableHolder.password_match);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Boolean isVerified(String Userinfo, String Password, EditText UserInfo, EditText password) {
        if (Userinfo.isEmpty()) {
            errorSetterAndFocus(UserInfo, VariableHolder.enter_username);
            return false;
        }
        if (Password.isEmpty()) {
            errorSetterAndFocus(password, VariableHolder.password_empty);
            return false;
        }
        if (Password.length() <= 6) {
            errorSetterAndFocus(password, VariableHolder.password_length);
            return false;
        }
        return true;
    }

    public static void alert(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(VariableHolder.Warning);
        builder.setCancelable(false);
        builder.setNeutralButton(VariableHolder.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static String encodeData(String Data){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String encodedData;
            encodedData=Base64.getEncoder().encodeToString(Data.getBytes());
            return encodedData;
        }
        return null;
    }

    public static String decodeData(String encodedData) {
        if (encodedData == null) {
            Log.e("Util", "Encoded data is null");
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                String decodedData = new String(Base64.getDecoder().decode(encodedData.getBytes()));
                Log.v("Util", "Decoded data: " + decodedData);
                return decodedData;
            } catch (IllegalArgumentException e) {
                Log.v("Util", "Failed to decode data: " + encodedData, e);
                return null;
            }
        }
        Log.e("Util", "Build version is below O");
        return null;
    }


}
