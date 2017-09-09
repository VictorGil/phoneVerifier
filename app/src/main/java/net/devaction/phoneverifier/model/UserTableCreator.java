package net.devaction.phoneverifier.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Víctor Gil
 */
public class UserTableCreator{
    private static final String TAG = "UserTableCreator";

    public static void create(SQLiteDatabase db){
        Log.d(TAG, "Going to create table: " + UserTable.NAME);

        db.execSQL("create table " + UserTable.NAME + "(" +
                //We do not really need the "unique" here because
                //there is going to be just one row in the table at most
                UserTable.Cols.PHONE_NUMBER + " text not null unique" +
                ")"
        );
    }
}
