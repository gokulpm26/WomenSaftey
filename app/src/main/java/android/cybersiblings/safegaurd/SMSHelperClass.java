package android.cybersiblings.safegaurd;

import android.app.PendingIntent;
import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMSHelperClass {
    private Context context;

    public SMSHelperClass(Context context) {
        this.context = context;
    }

    public void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
