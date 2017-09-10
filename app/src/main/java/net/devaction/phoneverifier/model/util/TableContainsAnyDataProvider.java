package net.devaction.phoneverifier.model.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Víctor Gil
 */
public class TableContainsAnyDataProvider {
    private static final String TAG = "TableContainsAnyDataPer";
    private static final String LONG_TAG = "TableContainsAnyDataProvider";

    public static boolean provide(Context context, String tableName, String columnName){
        Log.d(TAG, LONG_TAG  + ": Going to check " + tableName + " table, column name: " + columnName);
        boolean result = false;

        SQLiteDatabase database = DatabaseProvider.provideReadableDatabase(context);
        Cursor cursor = database.query(tableName, new String[]{columnName}, null, null, null, null, null);
        if (cursor != null){
            try{
                if (cursor.moveToFirst()){
                    result = true;
                }
            } finally{
                cursor.close();
            }
        }
        Log.d(TAG, "result: " + result);
        return result;
    }
}
