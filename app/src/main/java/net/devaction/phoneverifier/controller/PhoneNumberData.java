package net.devaction.phoneverifier.controller;

/**
 * @author Víctor Gil
 */
public class PhoneNumberData{
    boolean phoneNumbersMatch;

    boolean phoneNumberTooShort;

    boolean phoneNumberTooLong;

    String userPhoneNumber;

    public boolean isPhoneNumberTooShort() {
        return phoneNumberTooShort;
    }

    //regular getters and setters
    public boolean isPhoneNumberTooLong() {
        return phoneNumberTooLong;
    }

    public void setPhoneNumberTooLong(boolean phoneNumberTooLong) {
        this.phoneNumberTooLong = phoneNumberTooLong;
    }

    public void setPhoneNumberTooShort(boolean phoneNumberTooShort) {
        this.phoneNumberTooShort = phoneNumberTooShort;
    }

    public boolean doPhoneNumbersMatch(){
        return phoneNumbersMatch;
    }

    void setPhoneNumbersMatch(boolean phoneNumbersMatch) {
        this.phoneNumbersMatch = phoneNumbersMatch;
    }

    public String getUserPhoneNumber(){
        return userPhoneNumber;
    }

    void setUserPhoneNumber(String userPhoneNumber){
        this.userPhoneNumber = userPhoneNumber;
    }
}
