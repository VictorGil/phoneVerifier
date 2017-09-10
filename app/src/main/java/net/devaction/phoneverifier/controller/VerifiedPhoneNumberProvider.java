package net.devaction.phoneverifier.controller;

import android.content.Context;

import net.devaction.phoneverifier.model.NumberFromDbProvider;

/**
 * @author Víctor Gil
 */
public class VerifiedPhoneNumberProvider {

    public static String provide(Context context){
        return NumberFromDbProvider.provide(context);
    }
}
