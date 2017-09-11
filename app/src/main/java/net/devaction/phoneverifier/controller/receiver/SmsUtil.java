package net.devaction.phoneverifier.controller.receiver;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.UUID;

import net.devaction.phoneverifier.R;
import net.devaction.phoneverifier.model.GlobalConstants;
import net.devaction.phoneverifier.view.MainActivity;

/**
 * @author Víctor Gil
 */
public class SmsUtil{
    public static final String TAG = "SmsUtil";

    public static final String PHONE_NUMBER_EXTRA = "phoneNumber";
    public static final String VERIFICATION_CODE_EXTRA = "verificationCode";

    public static void sendSMS(String phoneNumber, Context context){
        Log.d(TAG, "Going to send an SMS message to: " + phoneNumber + ". Current thread name: "
                + Thread.currentThread().getName());
        String uuid = UUID.randomUUID().toString();

        //last 6 chars of the UUID are enough
        String verificationCode = uuid.substring(uuid.length() - 6);

        Intent sentIntent = new Intent(SmsSentBroadcastReceiver.SENT_ACTION).putExtra(PHONE_NUMBER_EXTRA, phoneNumber).
                putExtra(VERIFICATION_CODE_EXTRA, verificationCode);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);


        Intent deliveredIntent = new Intent(SmsDeliveredBroadcastReceiver.DELIVERED_ACTION).putExtra(PHONE_NUMBER_EXTRA, phoneNumber).
                putExtra(VERIFICATION_CODE_EXTRA, verificationCode);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, deliveredIntent, 0);

        final SmsManager smsManager = SmsManager.getDefault();
        String messageContent =  getMessageContent(phoneNumber, context, verificationCode);
        Log.d(TAG, "Content of message to be sent: " + messageContent);
        smsManager.sendTextMessage(phoneNumber, null, messageContent, sentPI, deliveredPI);

        Log.d(TAG, "SMS message sent to: " + phoneNumber);
    }

    static String getMessageContent(final String phoneNumber, final Context context, final String verificationCode){
        return context.getString(R.string.number_verification_message, GlobalConstants.APPLICATION_NAME, phoneNumber, verificationCode);
    }

    //We wait for the message to be received in a background thread
    public static NumberVerifierAsyncTask checkReceived(final String phoneNumber, final String verificationCode,
            final Context context, final MainActivity mainActivity){
        NumberVerifierAsyncTask numberVerifierAsyncTask = new NumberVerifierAsyncTask();
        numberVerifierAsyncTask.setMainActivity(mainActivity);
        numberVerifierAsyncTask.execute(phoneNumber, verificationCode, context);
        return numberVerifierAsyncTask;
    }
}
