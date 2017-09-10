package net.devaction.phoneverifier.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import net.devaction.phoneverifier.R;
import net.devaction.phoneverifier.controller.EnterPhoneNumberChecker;
import net.devaction.phoneverifier.controller.PhoneNumberData;

/**
 * @author Víctor Gil
 */
public class EnterPhoneDialogFragment extends DialogFragment{
    String phoneNumberString;
    String phoneNumberAgainString;

    AlertDialog dialog;
    View dialogView;
    EditText enterPhone;
    EditText enterPhoneAgain;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.enter_phone_dialog, null);

        setUpEditTexts();

        builder.setView(dialogView)
                .setPositiveButton(R.string.continue_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        PhoneNumberData phoneNumberData = EnterPhoneNumberChecker.phoneNumbersMatch(phoneNumberString, phoneNumberAgainString);
                        phoneNumberData = EnterPhoneNumberChecker.phoneNumberTooShort(phoneNumberData);
                        phoneNumberData = EnterPhoneNumberChecker.phoneNumberTooLong(phoneNumberData);
                        ((MainActivity) getActivity()).receivePhoneNumberDataFromDialogFragment(phoneNumberData);
                    }
                });
        dialog = builder.create();
        return dialog;
    }

    void setUpEditTexts(){
        enterPhone = (EditText) dialogView.findViewById(R.id.enter_phone);
        enterPhone.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        phoneNumberString = s.toString();
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });//end of anonymous inner class
        enterPhoneAgain = (EditText) dialogView.findViewById(R.id.enter_phone_again);

        enterPhoneAgain.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        phoneNumberAgainString = s.toString();
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
    }
}
