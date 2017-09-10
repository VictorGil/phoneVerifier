package net.devaction.phoneverifier.controller.receiver;
import static net.devaction.phoneverifier.controller.receiver.SmsUtil.PHONE_NUMBER_EXTRA;
import static net.devaction.phoneverifier.controller.receiver.SmsUtil.VERIFICATION_CODE_EXTRA;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Victor Gil
 * */
public class SmsDeliveredBroadcastReceiver extends BroadcastReceiver{
    private static final String TAG = "SmsDeliveredBroadcastRv";
    public final static String DELIVERED_ACTION = "COMPARTE_LOTERIA_VERIFICATION_SMS_DELIVERED";
    private boolean isRegistered;

    public void register(final Context context) {
        if (!isRegistered){
            Log.d(TAG, " going to register this broadcast receiver");
            context.registerReceiver(this, new IntentFilter(DELIVERED_ACTION));
            isRegistered = true;
        }
    }

    public void unregister(Context context) {
        if (isRegistered) {
            Log.d(TAG, " going to unregister this broadcast receiver");
            context.unregisterReceiver(this);
            isRegistered = false;
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        switch (getResultCode()){
            case Activity.RESULT_OK:
                Toast.makeText(context, "SMS entregado", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SmsDeliveredBroadcastReceiver: SMS has been delivered");
                String phoneNumber = intent.getStringExtra(PHONE_NUMBER_EXTRA);
                String verificationCode = intent.getStringExtra(VERIFICATION_CODE_EXTRA);
                Log.d(TAG, "phoneNumber from intent: " + phoneNumber);
                Log.d(TAG, "verification code from intent: " + verificationCode);
                SmsUtil.checkReceived(phoneNumber, verificationCode, context);
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(context, "SMS no entregado",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SmsDeliveredBroadcastReceiver: SMS has not been delivered");
                break;
        }
    }
}
