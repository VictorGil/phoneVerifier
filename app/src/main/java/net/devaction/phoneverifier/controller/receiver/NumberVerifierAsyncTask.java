package net.devaction.phoneverifier.controller.receiver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import net.devaction.phoneverifier.R;
import net.devaction.phoneverifier.controller.EnterPhoneNumberChecker;
import net.devaction.phoneverifier.controller.PhoneNumberVerifier;
import net.devaction.phoneverifier.view.MainActivity;

/**
 * @author Víctor Gil
 */
public class NumberVerifierAsyncTask extends AsyncTask<Object, Void, Boolean>{
    private static final String TAG = "NumberVerifierAsyncTask";
    private static final int MESSAGE_TYPE_INBOX = 1;
    private static final int SLEEP_TIME = 5000; //5 seconds

    private MainActivity mainActivity;

    @Override
    protected Boolean doInBackground(Object ... params) {
        String phoneNumber = (String) params[0];
        String verificationCode = (String) params[1];
        Context context = (Context) params[2];

        Log.d(TAG, "phoneNumber: " + phoneNumber);
        Log.d(TAG, "verificationCode: " + verificationCode);
        Log.d(TAG, "sleeping a few seconds to make sure the new message is in the inbox");
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException ex) {
            Log.e(TAG, ex.toString(), ex);
        }

        Uri smsMessagesUri = Uri.parse("content://sms/");
        ContentResolver cr = context.getContentResolver();

        Cursor cursor = null;
        boolean messageFound = false;
        try{
            cursor = cr.query(smsMessagesUri, null, "type =" + MESSAGE_TYPE_INBOX, null, "date DESC");
            int totalSmsMessages = cursor.getCount();
            Log.d(TAG, "Number of SMS messages: " + totalSmsMessages);

            if (cursor.moveToFirst()) {
                do{
                    String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    if (body.contains(verificationCode)){
                        Log.i(TAG, "Verification code: " + verificationCode +
                                " is contained in the message body: " + body );
                        if (address.contains(EnterPhoneNumberChecker.getLast9digits(phoneNumber))){
                            Log.i(TAG, "Mobile phone number: " + address + " has been verified." +
                                    " Number entered by user: " + phoneNumber);
                            messageFound = true;
                        }
                    }
                } while(cursor.moveToNext() && !messageFound);
            }
        } finally{
            if (cursor != null)
                cursor.close();
        }
        if (messageFound){
            PhoneNumberVerifier.verify(context, phoneNumber);
            Log.d(TAG, "Current thread name inside doInBackground method: " + Thread.currentThread().getName());
        }
        return messageFound;
    }

    @Override
    protected void onPostExecute(Boolean messageFound) {
        if (messageFound){
            Log.d(TAG, "Current thread name inside onPostExecute method: " + Thread.currentThread().getName());
            Toast.makeText(mainActivity, R.string.number_has_been_verified, Toast.LENGTH_LONG).show();
            if (mainActivity != null){
                mainActivity.refreshView();
            }
        }
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
