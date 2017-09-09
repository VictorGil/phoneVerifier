package net.devaction.phoneverifier.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import net.devaction.phoneverifier.R;

/**
 * @author Víctor Gil
 */
public class PermissionsRationaleDialogFragment extends DialogFragment{
    AlertDialog dialog;
    View dialogView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.permissions_rationale_dialog, null);

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.verify_number, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //We could have used an interface instead of MainActivity here, but
                        // it is not worth it for this simple app.
                        ((MainActivity) getActivity()).userAcceptedToVerifyNumber();
                    }
                })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       getDialog().cancel();
                   }
               });
        dialog = builder.create();
        return dialog;
    }
}
