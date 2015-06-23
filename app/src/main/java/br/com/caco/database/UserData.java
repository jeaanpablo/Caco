package br.com.caco.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jean Pablo Bosso on 23/06/2015.
 */
public class UserData extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "USer";
    public static final String DATABASE_NAME ="caco.db";
    private static final int DATABASE_VERSION = 1;

    public UserData(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(_id integer primary key autoincrement, token text not null, permission text not null, id_user integer not null);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
