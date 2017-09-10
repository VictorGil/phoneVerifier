package net.devaction.phoneverifier.controller.receiver;
import java.util.Arrays;
import java.util.concurrent.Callable;

import net.devaction.phoneverifier.controller.EnterPhoneNumberChecker;
import net.devaction.phoneverifier.controller.PhoneNumberVerifier;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author Víctor Gil
 */
public class CallableNumberVerificator implements Callable<Object>{
    private static final String TAG = "CallableNumVerificator";
    private static final int MESSAGE_TYPE_INBOX = 1;
    private static final int SLEEP_TIME = 5000; //5 seconds

    String phoneNumber;
    String verificationCode;
    Context context;

    public CallableNumberVerificator(String phoneNumber, String verificationCode, Context context){
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.context = context;
    }

    //it will just return null
    //we just want to notify that the Future task finishes
    @Override
    public Object call() {
        Log.d(TAG, "phoneNumber: " + phoneNumber);
        Log.d(TAG, "verificationCode: " + verificationCode);
        Log.d(TAG, "sleeping a few seconds to make sure the new message is in the inbox");
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException ex) {
            Log.e(this.toString(), ex + Arrays.toString(ex.getStackTrace()));
        }
        Uri smsMessagesUri = Uri.parse("content://sms/");
        ContentResolver cr = context.getContentResolver();

        Cursor cursor = null;
        boolean messageFound = false;
        try{
            cursor = cr.query(smsMessagesUri, null, "type =" + MESSAGE_TYPE_INBOX, null, "date DESC");
            int totalSmsMessages = cursor.getCount();
            Log.d("RNVerificator", "RunnableNumberVerificator: Number of SMS messages: " + totalSmsMessages);

            if (cursor.moveToFirst()) {
                do{
                    String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    if (body.contains(verificationCode)){
                        Log.i(TAG, "RunnableNumberVerificator: Verification code: " + verificationCode +
                                " is contained in the message body: " + body );
                        if (address.contains(EnterPhoneNumberChecker.getLast9digits(phoneNumber))){
                            Log.i(this.toString(), "Mobile phone number: " + address + " has been verified." +
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
        }
        return null;
    }
}
