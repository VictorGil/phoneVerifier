package net.devaction.phoneverifier.controller.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import static net.devaction.phoneverifier.controller.receiver.SmsUtil.PHONE_NUMBER_EXTRA;
import static net.devaction.phoneverifier.controller.receiver.SmsUtil.VERIFICATION_CODE_EXTRA;

/**
 * @author Víctor Gil
 */
public class SmsSentBroadcastReceiver extends BroadcastReceiver{
    public static final String TAG = "SmsSentBroadcastRver";
    public static final String SENT_ACTION = "NET_DEVACTION_PHONEVERIFIER_VERIFICATION_SMS_SENT";

    private boolean isRegistered;

    public void register(final Context context) {
        if (!isRegistered){
            Log.d(this.toString(), "going to register this broadcast receiver");
            context.registerReceiver(this, new IntentFilter(SENT_ACTION));
            isRegistered = true;
        }
    }

    public void unregister(Context context) {
        if (isRegistered) {
            Log.d(TAG, "going to unregister this broadcast receiver");
            context.unregisterReceiver(this);
            isRegistered = false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()){
            case Activity.RESULT_OK:
                Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SmsSentBroadcastReceiver: SMS has been sent");
                String phoneNumber = intent.getStringExtra(PHONE_NUMBER_EXTRA);
                String verificationCode = intent.getStringExtra(VERIFICATION_CODE_EXTRA);
                Log.d(TAG, "phoneNumber from intent: " + phoneNumber);
                Log.d(TAG, "verification code from intent: " + verificationCode);
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(context, "Fallo genérico", Toast.LENGTH_SHORT).show();
                Log.d("SmsSentBroadcastRver", "SMS has not been sent, there was a generic failure");
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(context, "No network service", Toast.LENGTH_SHORT).show();
                Log.d("SmsSentBroadcastRver", "SMS has not been sent, there is no service");
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SMS has not been sent, there is no PDU");
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SMS has not been sent, radio is off");
                break;
        }
    }
}