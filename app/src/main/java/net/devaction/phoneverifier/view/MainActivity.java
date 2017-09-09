package net.devaction.phoneverifier.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.devaction.phoneverifier.R;
import net.devaction.phoneverifier.controller.PhoneNumberChecker;
import net.devaction.phoneverifier.controller.PhoneNumberProvider;
import net.devaction.phoneverifier.controller.PhoneNumberUnverifier;

/**
 * @author Victor Gil
 * */
public class MainActivity extends AppCompatActivity {
    public static final String APPLICATION_NAME = "phoneVerifier";
    private static final String TAG = "MainActivity";

    private Button doSomethingButton;
    private Button unverifyNumberButton;
    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //By pressing this button, the user wants to do something (whatever),
        //and to do that something, her/his phone number must be verified
        doSomethingButton = (Button) findViewById(R.id.do_something_button);
        doSomethingButton.setOnClickListener(new doSomethingOnClickListener());

        //the user wants to unverify their phone number
        unverifyNumberButton = (Button) findViewById(R.id.unverify_number_button);
        unverifyNumberButton.setOnClickListener(new UnverifyNumberOnClickListener());
        makeUnverifyNumberButtonLookDisabledIfRequired();

        messageTextView = (TextView) findViewById(R.id.message_text_view);
        changeTextViewMessageIfRequired();
    }

    private class doSomethingOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            if(IsNumberVerified()){
                Toast.makeText(MainActivity.this,R.string.done_toast, Toast.LENGTH_SHORT).show();
            } else
                showNumberVerificationPermissionsRationale();
        }
    }

    private class UnverifyNumberOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
             if(IsNumberVerified()){
                 PhoneNumberUnverifier.unverify(getApplicationContext());
                 makeUnverifyNumberButtonLookDisabledIfRequired();
                 Toast.makeText(MainActivity.this,R.string.phone_number_unverified_toast, Toast.LENGTH_SHORT).show();
             } else
                 Toast.makeText(MainActivity.this,R.string.phone_number_already_unverified_toast, Toast.LENGTH_SHORT).show();

        }
    }

    private boolean IsNumberVerified(){
        return PhoneNumberChecker.isVerified(getApplicationContext());
    }

    private void changeTextViewMessageIfRequired(){
        if (IsNumberVerified()){
            String knownPhoneNumberPattern = getResources().getString(R.string.known_phone_number);
            messageTextView.setText(String.format(knownPhoneNumberPattern,
                    PhoneNumberProvider.provide(getApplicationContext())));
        }
    }

    private void makeUnverifyNumberButtonLookDisabledIfRequired(){
        if (!IsNumberVerified())
            unverifyNumberButton.setAlpha(.5f);
    }

    private void showNumberVerificationPermissionsRationale(){
        new PermissionsRationaleDialogFragment().show(this.getSupportFragmentManager(),
                "PermissionsRationaleDialogFragment");
    }

    //this method is called by PermissionsRationaleDialogFragment
    public void userAcceptedToVerifyNumber(){

    }
}
