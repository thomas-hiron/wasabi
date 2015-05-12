package project.gobelins.wasabi.fresco;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import project.gobelins.wasabi.entities.Drawing;
import project.gobelins.wasabi.entities.Entity;
import project.gobelins.wasabi.fresco.drawing.ColorPoint;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.sqlite.tables.Drawings;
import project.gobelins.wasabi.utils.DateFormater;

/**
 * Permet de récupérer tous les dessins
 * <p/>
 * Created by ThomasHiron on 12/05/2015.
 */
public class DrawingsManager
{
    private ContentResolver mContentResolver;

    public DrawingsManager(ContentResolver contentResolver)
    {
        mContentResolver = contentResolver;
    }

    /**
     * Récupération des dessins
     */
    public HashMap<Date, ArrayList<Entity>> getDrawings()
    {
        Cursor c = mContentResolver.query(Uri.parse(Drawings.URL_DRAWINGS), null, null, null, null);

        HashMap<Date, ArrayList<Entity>> drawings = new HashMap<>();

        if(c.moveToFirst())
        {
            do
            {
                /* Instanciation de la notification */
                Drawing drawing = new Drawing();
                drawing.setId(c.getInt(c.getColumnIndex(Drawings.DRAWINGS_ID)));
                drawing.setDate(c.getString(c.getColumnIndex(Drawings.DRAWINGS_DATE)));
                drawing.setColor(c.getInt(c.getColumnIndex(Drawings.DRAWINGS_COLOR)));
                drawing.setPoints(c.getString(c.getColumnIndex(Drawings.DRAWINGS_POINTS)));

                /* Récupération et initialisation si null */
                ArrayList<Entity> dateDrawings = drawings.get(drawing.getDate());
                if(dateDrawings == null)
                    dateDrawings = new ArrayList<>();

                /* Ajout du dessin */
                dateDrawings.add(drawing);
                drawings.put(drawing.getDate(), dateDrawings);
            }
            while(c.moveToNext());
        }

        return drawings;
    }

    /**
     * Enregistre un dessin
     *
     * @param points Les points
     */
    public void saveDrawing(ArrayList<Point> points)
    {
        /* La couleur */
        int color = ((ColorPoint) points.get(0)).getColor();

        /* Nouvelles valeurs */
        ContentValues contentValues = new ContentValues(3);
        contentValues.put(Drawings.DRAWINGS_DATE, DateFormater.getTodayAsString());
        contentValues.put(Drawings.DRAWINGS_POINTS, points.toString().replaceAll("\\[(.*)\\]", "$1"));
        contentValues.put(Drawings.DRAWINGS_COLOR, color);

        /* Insertion */
        mContentResolver.insert(Uri.parse(Drawings.URL_DRAWINGS), contentValues);
    }

    /**
     * Supprime le dernier dessin
     */
    public void deleteLastDraw()
    {
        /* Condition pour le dernier */
        String condition = Drawings.DRAWINGS_ID + " = (SELECT MAX(" + Drawings.DRAWINGS_ID + ") FROM " + Drawings.TABLE_DRAWINGS + ")";

        /* Suppression */
        mContentResolver.delete(Uri.parse(Drawings.URL_DRAWINGS), condition, null);
    }
}