package net.devaction.phoneverifier.controller;

import android.content.Context;

import net.devaction.phoneverifier.model.NumberInDbInserter;

/**
 * @author Víctor Gil
 */
public class PhoneNumberVerifier{
    public static void verify(Context context, String number){
        NumberInDbInserter.insert(context, number);
    }
}
