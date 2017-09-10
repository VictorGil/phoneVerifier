package net.devaction.phoneverifier.model.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Víctor Gil
 */
public class AllDataInTableDeleter{
    public static void delete(Context context, String tableName){
        SQLiteDatabase database = DatabaseProvider.provideWritableDatabase(context);
        database.delete(tableName, null, null);
    }
}
