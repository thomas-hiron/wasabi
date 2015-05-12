package project.gobelins.wasabi.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import project.gobelins.wasabi.sqlite.tables.Drawings;
import project.gobelins.wasabi.sqlite.tables.Images;
import project.gobelins.wasabi.sqlite.tables.Notifications;

/**
 * Created by ThomasHiron on 24/10/2014.
 *
 * Exécute les requêtes
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    /**
     * Constructeur
     *
     * @param context Le contexte
     */
    public DatabaseHelper(Context context)
    {
        super(context, ContentProvider.DATABASE_NAME, null, ContentProvider.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(Notifications.CREATE_TABLE_NOTIFICATIONS);
        sqLiteDatabase.execSQL(Drawings.CREATE_TABLE_DRAWINGS);
        sqLiteDatabase.execSQL(Images.CREATE_TABLE_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        /* Suppression des tables */
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Notifications.TABLE_NOTIFICATIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Drawings.TABLE_DRAWINGS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Images.TABLE_IMAGES);

        /* Recréation */
        onCreate(sqLiteDatabase);
    }
}