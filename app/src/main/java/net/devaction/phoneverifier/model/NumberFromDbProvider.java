package net.devaction.phoneverifier.model;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.devaction.phoneverifier.model.util.DatabaseProvider;

/**
 * @author Víctor Gil
 */
public class NumberFromDbProvider{
    private static final String TAG = "NumberFromDbProvider";

    public static String provide(Context context){
        String phoneNumber = null;

        SQLiteDatabase database = DatabaseProvider.provideReadableDatabase(context);
        Cursor cursor = database.query(UserTable.NAME,
                null, // Columns - null selects all columns
                null, //whereClause
                null, //whereArgs
                null, // groupBy
                null, // having
                null);  // orderBy

        if (cursor != null){
            try{
                if (cursor.moveToFirst()){
                    CursorWrapper cursorWrapper = new CursorWrapper(cursor);
                    phoneNumber = cursorWrapper.getString(
                            cursorWrapper.getColumnIndex(UserTable.Cols.PHONE_NUMBER));
                }
            } finally{
                cursor.close();
            }
        }
        Log.d(TAG, "Phone number retrieved from DB: " + phoneNumber);
        return phoneNumber;
    }
}
