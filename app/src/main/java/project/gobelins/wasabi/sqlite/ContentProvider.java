package project.gobelins.wasabi.sqlite;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import project.gobelins.wasabi.sqlite.tables.Notifications;

/**
 * Created by ThomasHiron on 24/10/2014.
 *
 * Interagit avec le DatabaseHelper et construit les requêtes
 */
public class ContentProvider extends android.content.ContentProvider
{
    public static final String PROVIDER_NAME = "project.gobelins.wasabi.provider.Wasabi";

    /* Les valeurs utilisées dans le content uri */
    private static final int NOTIFICATIONS = 1;

    /* L'helper sqlite */
    DatabaseHelper mDBHelper;

    private static final UriMatcher mUriMatcher;
    static
    {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(PROVIDER_NAME, Notifications.TABLE_NOTIFICATIONS, NOTIFICATIONS);
    }

    /* LA BDD */
    private SQLiteDatabase mDatabase;
    protected static final String DATABASE_NAME = "wasabi";
    protected static final int DATABASE_VERSION = 1;

    @Override
    public boolean onCreate()
    {
        mDBHelper = new DatabaseHelper(getContext());

        mDatabase = mDBHelper.getWritableDatabase();

        return mDatabase != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        /* Le constructeur de requête */
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        /* Si on récupère les playlists ou les sons */
        switch(mUriMatcher.match(uri))
        {
            /* Les playlists */
            case NOTIFICATIONS:

                queryBuilder.setTables(Notifications.TABLE_NOTIFICATIONS);
                sortOrder = sortOrder == null ? Notifications.NOTIFICATIONS_ID : sortOrder;

                break;

            /* Erreur */
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        /* Exécution */
        Cursor cursor = queryBuilder.query(mDatabase, projection, selection, selectionArgs, null, null, sortOrder);

        /* register to watch a content URI for changes */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        /* Retour des résultats */
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        /* Récupération de l'id */
        int id = mUriMatcher.match(uri);

        /* La table et le content uri*/
        String table = null;
        Uri contentUri = null;

        if(id == NOTIFICATIONS)
        {
            table = Notifications.TABLE_NOTIFICATIONS;
            contentUri = Notifications.CONTENT_URI_NOTIFICATIONS;
        }

        /* Insertion et récupération de l'id inséré */
        long row = mDatabase.insert(table, "", contentValues);

        /* Ligne bien insérée */
        if(row > 0)
        {
            Uri newUri = ContentUris.withAppendedId(contentUri, row);
            getContext().getContentResolver().notifyChange(newUri, null);

            return newUri;
        }
        throw new SQLException("Fail to add a new record into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int count;

        switch(mUriMatcher.match(uri))
        {
            case NOTIFICATIONS:

                count = mDatabase.delete(Notifications.TABLE_NOTIFICATIONS, selection, selectionArgs);

                break;

            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }
}
