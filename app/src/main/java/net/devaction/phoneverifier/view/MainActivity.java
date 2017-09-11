package net.devaction.phoneverifier.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.devaction.phoneverifier.R;
import net.devaction.phoneverifier.controller.PhoneNumberChecker;
import net.devaction.phoneverifier.controller.PhoneNumberData;
import net.devaction.phoneverifier.controller.PhoneNumberVerifier;
import net.devaction.phoneverifier.controller.VerifiedPhoneNumberProvider;
import net.devaction.phoneverifier.controller.PhoneNumberUnverifier;
import net.devaction.phoneverifier.controller.receiver.SmsDeliveredBroadcastReceiver;
import net.devaction.phoneverifier.controller.receiver.SmsSentBroadcastReceiver;
import net.devaction.phoneverifier.controller.receiver.SmsUtil;

/**
 * @author Victor Gil
 * */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button doSomethingButton;
    private Button unverifyNumberButton;
    private TextView messageTextView;

    //This "magic" prefix allows you to verify a "fake" phone number without sending an SMS
    //useful for testing
    private static final String MAGIC_PREFIX = "999999";

    private SmsSentBroadcastReceiver smsSentBroadcastReceiver;
    private SmsDeliveredBroadcastReceiver smsDeliveredBroadcastReceiver;

    private PhoneNumberData phoneNumberData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //By pressing this button, the user wants to do something (whatever),
        //and to do that something, her/his phone number must be verified first
        doSomethingButton = (Button) findViewById(R.id.do_something_button);
        doSomethingButton.setOnClickListener(new doSomethingOnClickListener());

        //the user wants to unverify their phone number
        unverifyNumberButton = (Button) findViewById(R.id.unverify_number_button);
        unverifyNumberButton.setOnClickListener(new UnverifyNumberOnClickListener());

        messageTextView = (TextView) findViewById(R.id.message_text_view);

        refreshView();
    }

    private class doSomethingOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            if(IsNumberVerified()){
                Toast.makeText(MainActivity.this, R.string.done_toast, Toast.LENGTH_SHORT).show();
            } else{
                if (isVerificationOngoing())
                    Toast.makeText(MainActivity.this, R.string.previous_verification_ongoing_toast, Toast.LENGTH_SHORT).show();
                else
                    showNumberVerificationPermissionsRationale();
            }
        }
    }

    private boolean isVerificationOngoing(){
        if (smsDeliveredBroadcastReceiver == null)
            return false;
        return smsDeliveredBroadcastReceiver.isVerificationOngoing();
    }

    private class UnverifyNumberOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
             if (IsNumberVerified()){
                 PhoneNumberUnverifier.unverify(getApplicationContext());
                 Toast.makeText(MainActivity.this,R.string.phone_number_unverified_toast, Toast.LENGTH_SHORT).show();
             } else{
                 Toast.makeText(MainActivity.this,R.string.phone_number_already_unverified_toast, Toast.LENGTH_SHORT).show();
             }
             refreshView();
        }
    }

    private boolean IsNumberVerified(){
        return PhoneNumberChecker.isVerified(getApplicationContext());
    }


    public void refreshView(){
        if (IsNumberVerified()){
            setKnowNumberTextViewMessage();
            makeUnverifyNumberButtonLookEnabled();
        } else{
            setUnknownPhoneNumberTextViewMessage();
            makeUnverifyNumberButtonLookDisabled();
        }
    }

    private void setKnowNumberTextViewMessage(){
        String knownPhoneNumberText = getResources().getString(R.string.known_phone_number,
                VerifiedPhoneNumberProvider.provide(getApplicationContext()));
        messageTextView.setText(knownPhoneNumberText);
    }

    private void setUnknownPhoneNumberTextViewMessage(){
        messageTextView.setText(getResources().getString(R.string.unknown_phone_number));
    }

    private void makeUnverifyNumberButtonLookDisabled(){
        unverifyNumberButton.setAlpha(.5f);
    }


    private void makeUnverifyNumberButtonLookEnabled(){
        unverifyNumberButton.setAlpha(1);
    }

    private void showNumberVerificationPermissionsRationale(){
        new PermissionsRationaleDialogFragment().show(this.getSupportFragmentManager(),
                "PermissionsRationaleDialogFragment");
    }

    //this method is called by PermissionsRationaleDialogFragment
    public void userAcceptedToVerifyNumber(){
        if (IsNumberVerified()){
            Toast.makeText(MainActivity.this,R.string.phone_number_already_verified_toast, Toast.LENGTH_SHORT).show();
            makeUnverifyNumberButtonLookEnabled();
            return;
        }
        if (isVerificationOngoing()){
            Toast.makeText(MainActivity.this,R.string.previous_verification_ongoing_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        showEnterPhoneDialogFragment();
    }

    private void showEnterPhoneDialogFragment(){
        new EnterPhoneDialogFragment().show(this.getSupportFragmentManager(), "EnterPhoneDialogFragment");
    }

    private boolean isMagicNumber(String phoneNumber){
        if (phoneNumber.contains(MAGIC_PREFIX))
            return true;
        return false;
    }

    public void receivePhoneNumberDataFromDialogFragment(PhoneNumberData phoneNumberData){
        if (phoneNumberData == null){
            showEnterPhoneDialogFragment();
            return;
        }
        if (!phoneNumberData.doPhoneNumbersMatch()){
            Toast.makeText(MainActivity.this, R.string.phoneNumbersDontMatch,
                        Toast.LENGTH_LONG).show();
            showEnterPhoneDialogFragment();
            return;
        }
        if (phoneNumberData.isPhoneNumberTooShort()) {
            Toast.makeText(MainActivity.this, R.string.phoneNumberIsTooShort,
                    Toast.LENGTH_LONG).show();
            showEnterPhoneDialogFragment();
            return;
        }
        if (phoneNumberData.isPhoneNumberTooLong()) {
            Toast.makeText(MainActivity.this, R.string.phoneNumberIsTooLong,
                    Toast.LENGTH_LONG).show();
            showEnterPhoneDialogFragment();
            return;
        }

        //There is a magic phone number prefix for testing which gets verified without sending and
        //receiving an SMS message
        if (isMagicNumber(phoneNumberData.getUserPhoneNumber())){
            PhoneNumberVerifier.verify(getApplicationContext(), phoneNumberData.getUserPhoneNumber());
            refreshView();
            return;
        }

        this.phoneNumberData = phoneNumberData;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Registering broadcast receivers and sending SMS verification message inside the receivePhoneNumberDataFromDialogFragment method");
            registerBroadCastReceiversAndSendSms(phoneNumberData.getUserPhoneNumber());
        } else{
            askForSendReadSmsPermissionsIfRequired();
        }
    }

    private void registerBroacastReceivers(){
        if (smsSentBroadcastReceiver == null)
            smsSentBroadcastReceiver = new SmsSentBroadcastReceiver();
        if (smsDeliveredBroadcastReceiver == null)
            smsDeliveredBroadcastReceiver = new SmsDeliveredBroadcastReceiver();

        smsDeliveredBroadcastReceiver.register(getApplicationContext());
        // to pass a reference to the MainActivity object is not nice but it is a practical way to refresh the UI
        //right after the verification SMS has been received and the user phone number has just been verified
        smsDeliveredBroadcastReceiver.setMainActivity(MainActivity.this);

        smsSentBroadcastReceiver.register(getApplicationContext());
    }

    private void unregisterBroacastReceivers(){
        if (smsSentBroadcastReceiver != null)
            smsSentBroadcastReceiver.unregister(getApplicationContext());
        if (smsDeliveredBroadcastReceiver != null)
            smsDeliveredBroadcastReceiver.unregister(getApplicationContext());
    }

    private void askForSendReadSmsPermissionsIfRequired(){
        Log.d(TAG, "askForSendReadSmsPermissionsIfRequired() method started, going to check permissions");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
                //We ask for both SEND and READ permissions
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_SMS}, 0);
            else {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                    //We only ask for the SEND_SMS permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
                else{
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
                        //We only ask for the READ_SMS permission
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 2);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if (requestCode >= 0 && requestCode <=2){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionsResult: SEND and/or READ SMS permissions were not granted");
            }else{
                Log.d(TAG, "onRequestPermissionsResult: SEND and READ SMS permissions are granted now, " +
                        "going to registering broadcast receivers and send SMS verification message ");
                registerBroadCastReceiversAndSendSms(phoneNumberData.getUserPhoneNumber());
            }
        }
    }

    private void registerBroadCastReceiversAndSendSms(String phoneNumber){
        registerBroacastReceivers();
        SmsUtil.sendSMS(phoneNumber, getApplicationContext());
    }

    @Override
    protected void onStop(){
        unregisterBroacastReceivers();
        super.onStop();
    }
}
