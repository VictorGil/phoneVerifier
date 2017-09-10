package net.devaction.phoneverifier.controller;

import android.util.Log;

/**
 * @author Víctor Gil
 */
public class EnterPhoneNumberChecker{
    private static final String TAG = "EnterPhoneNumChecker";
    public static final int MIN_PHONE_NUMBER_lENGTH = 9;
    public static final int MAX_PHONE_NUMBER_lENGTH = 15;

    public static PhoneNumberData phoneNumbersMatch(final String phoneNumber, final String phoneNumberAgain){
        PhoneNumberData phoneNumberData = new PhoneNumberData();
        if (removeNonDigitChars(phoneNumber).equals(removeNonDigitChars(phoneNumberAgain))){
            phoneNumberData.setPhoneNumbersMatch(true);
            phoneNumberData.setUserPhoneNumber(removeNonDigitChars(phoneNumber));
        } else{
            Log.d(TAG, "phone numbers do not match: " + phoneNumber + " vs " + phoneNumberAgain);
            phoneNumberData.setPhoneNumbersMatch(false);
        }
        return phoneNumberData;
    }

    public static PhoneNumberData phoneNumberTooShort(PhoneNumberData phoneNumberData){
        if (phoneNumberData.getUserPhoneNumber() != null && phoneNumberData.getUserPhoneNumber().length() < MIN_PHONE_NUMBER_lENGTH){
            Log.d(TAG, "phone number is too short: " + phoneNumberData.getUserPhoneNumber());
            phoneNumberData.setPhoneNumberTooShort(true);
        } else{
            phoneNumberData.setPhoneNumberTooShort(false);
        }
        return phoneNumberData;
    }

    public static PhoneNumberData phoneNumberTooLong(PhoneNumberData phoneNumberData){
        if (phoneNumberData.getUserPhoneNumber() != null && phoneNumberData.getUserPhoneNumber().length() > MAX_PHONE_NUMBER_lENGTH){
            Log.d(TAG, "phone number is too long: " + phoneNumberData.getUserPhoneNumber());
            phoneNumberData.setPhoneNumberTooLong(true);
        } else{
            phoneNumberData.setPhoneNumberTooLong(false);
        }
        return phoneNumberData;
    }

    public static String removeNonDigitChars(final String phoneNumber){
        return phoneNumber.trim().replace("+", "00").replaceAll("\\D","");
    }

    public static String getLast9digits(String number){
        number = removeNonDigitChars(number);
        if (number.length() <= 9)
            return number;
        return number.substring(number.length() - 9);
    }
}
