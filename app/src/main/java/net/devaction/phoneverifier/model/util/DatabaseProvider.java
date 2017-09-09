package net.devaction.phoneverifier.model.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.devaction.phoneverifier.model.SQLiteHelper;

/**
 * @author Víctor Gil
 */
public class DatabaseProvider {
    public static SQLiteDatabase provideReadableDatabase(Context context){
        return SQLiteHelper.getInstance(context).getReadableDatabase();
    }

    public static SQLiteDatabase provideWritableDatabase(Context context){
        return SQLiteHelper.getInstance(context).getWritableDatabase();
    }
}
