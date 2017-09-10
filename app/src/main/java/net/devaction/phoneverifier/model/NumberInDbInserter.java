package net.devaction.phoneverifier.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.devaction.phoneverifier.model.util.DatabaseProvider;

/**
 * @author Víctor Gil
 */
public class NumberInDbInserter{
    private static final String TAG = "NumberInDbInserter";

    public static void insert(Context context, String phoneNumber){
        SQLiteDatabase database = DatabaseProvider.provideWritableDatabase(context);
        database.insert(UserTable.NAME, null, provideContentValues(phoneNumber));
        Log.d(TAG, "User phone numbner " + phoneNumber + " has been inserted into the database");
    }

    private static ContentValues provideContentValues(String phoneNumber){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.Cols.PHONE_NUMBER, phoneNumber);
        return contentValues;
    }
}
