package net.devaction.phoneverifier.controller.receiver;
import static net.devaction.phoneverifier.controller.receiver.SmsUtil.PHONE_NUMBER_EXTRA;
import static net.devaction.phoneverifier.controller.receiver.SmsUtil.VERIFICATION_CODE_EXTRA;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import static android.os.AsyncTask.Status.FINISHED;

import android.util.Log;
import android.widget.Toast;

import net.devaction.phoneverifier.R;
import net.devaction.phoneverifier.view.MainActivity;

/**
 * @author Victor Gil
 * */
public class SmsDeliveredBroadcastReceiver extends BroadcastReceiver{
    private static final String TAG = "SmsDeliveredBroadcastRv";
    public final static String DELIVERED_ACTION = "COMPARTE_LOTERIA_VERIFICATION_SMS_DELIVERED";
    private boolean isRegistered;

    //to have this reference here is not nice but it is a practical way to refresh the UI
    //right after the verification SMS has been received and the user phone number has been verified
    private MainActivity mainActivity;
    private NumberVerifierAsyncTask numberVerifierAsyncTask;

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
                Toast.makeText(context, R.string.SMS_delivered, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SmsDeliveredBroadcastReceiver: SMS has been delivered");
                String phoneNumber = intent.getStringExtra(PHONE_NUMBER_EXTRA);
                String verificationCode = intent.getStringExtra(VERIFICATION_CODE_EXTRA);
                Log.d(TAG, "phoneNumber from intent: " + phoneNumber);
                Log.d(TAG, "verification code from intent: " + verificationCode);
                Log.d(TAG, "Current thread name: " + Thread.currentThread().getName());
                numberVerifierAsyncTask = SmsUtil.checkReceived(phoneNumber, verificationCode, context, mainActivity);
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(context, R.string.SMS_not_delivered, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SMS has not been delivered");
                break;
        }
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public boolean isVerificationOngoing(){
        if (numberVerifierAsyncTask == null ||
                numberVerifierAsyncTask.getStatus() == FINISHED){
            numberVerifierAsyncTask = null;
            return false;
        }
        return true;
    }
}
