package net.devaction.phoneverifier.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static net.devaction.phoneverifier.view.MainActivity.APPLICATION_NAME;

/**
 *  @author Víctor Gil
 */
public class SQLiteHelper extends SQLiteOpenHelper{
    private static final String TAG = "SQLiteHelper";

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = APPLICATION_NAME + ".db";

    private static SQLiteHelper instance = null;

    public static SQLiteHelper getInstance(Context appContext){
        if (instance == null)
            instance = new SQLiteHelper(appContext);
        return instance;
    }

    private SQLiteHelper(Context appContext){
        super(appContext, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate method started");
        UserTableCreator.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
