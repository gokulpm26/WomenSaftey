package android.cybersiblings.safegaurd;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperClass extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SafeGuardDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "User_data";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone_number";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FAVORITE_CONTACT1 = "favorite_contact_1";
    private static final String COLUMN_FAVORITE_CONTACT2 = "favorite_contact_2";
    SharedPreferences sharedPreferences;

    private Context context;

    public DatabaseHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT PRIMARY KEY, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_DATE_OF_BIRTH + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_FAVORITE_CONTACT1 + " TEXT, " +
                COLUMN_FAVORITE_CONTACT2 + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addDataToDatabase(String name, String phone, String email, String dob, String password, String fav1, String fav2) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_DATE_OF_BIRTH, dob);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_FAVORITE_CONTACT1, fav1);
        contentValues.put(COLUMN_FAVORITE_CONTACT2, fav2);

        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            Toast.makeText(context, VariableHolder.Failed_to_store_data, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, VariableHolder.DataStoredSuccessfully, Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readData() {
        String query = " SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public Login_Status verifyUser(String enteredUserInfo, String enteredPassword) {
        SQLiteDatabase database = this.getReadableDatabase();
        Login_Status loginStatus = new Login_Status();
        loginStatus.setLogin_status(false);

        String query = "SELECT " + COLUMN_PHONE + ", " + COLUMN_PASSWORD + " FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String phone = Util.decodeData(cursor.getString(0));
                String password = Util.decodeData(cursor.getString(1));

                if (phone.equals(enteredUserInfo)) {
                    if (password.equals(enteredPassword)) {
                        loginStatus.setLogin_status(true);
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        loginStatus.setLogin_message(VariableHolder.Inavlid_password);
                    }
                } else {
                    loginStatus.setLogin_message(VariableHolder.phone_not_exixts);
                }
            } while (cursor.moveToNext());
        } else {
            loginStatus.setLogin_message(VariableHolder.UserDatanotfound);
        }
        cursor.close();
        return loginStatus;
    }

    void updateUserData(String name, String phone, String email, String dob, String password, String fav1, String fav2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PHONE, phone);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_DATE_OF_BIRTH, dob);
        cv.put(COLUMN_PASSWORD, password);
        cv.put(COLUMN_FAVORITE_CONTACT1, fav1);
        cv.put(COLUMN_FAVORITE_CONTACT2, fav2);


        long result = db.update(TABLE_NAME, cv, COLUMN_PHONE + " = ? ", new String[]{phone});
        if (result == -1) {
            Toast.makeText(context, VariableHolder.Failupdate, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, VariableHolder.Successfullyupdated, Toast.LENGTH_SHORT).show();
        }

    }
}
