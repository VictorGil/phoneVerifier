package net.devaction.phoneverifier.controller;

import android.content.Context;
import net.devaction.phoneverifier.model.UserTable;
import net.devaction.phoneverifier.model.util.AllDataInTableDeleter;

/**
 * Víctor Gil
 */
public class PhoneNumberUnverifier{

    public static void unverify(Context context){
        AllDataInTableDeleter.delete(context, UserTable.NAME);
    }
}
