package net.devaction.phoneverifier.controller;

import android.content.Context;

import net.devaction.phoneverifier.model.util.TableContainsAnyDataProvider;
import net.devaction.phoneverifier.model.UserTable;
import static net.devaction.phoneverifier.model.UserTable.Cols.PHONE_NUMBER;

/**
 * @author Víctor Gil
 */
public class PhoneNumberChecker{
    public static boolean isVerified(Context context){
        return TableContainsAnyDataProvider.provide(context, UserTable.NAME, PHONE_NUMBER);
    }
}
