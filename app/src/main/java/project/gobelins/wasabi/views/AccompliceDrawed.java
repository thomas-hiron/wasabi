package project.gobelins.wasabi.views;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.entities.Drawing;
import project.gobelins.wasabi.entities.Entity;
import project.gobelins.wasabi.fresco.drawing.DrawedView;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.sqlite.tables.AccompliceDrawing;
import project.gobelins.wasabi.sqlite.tables.Drawings;

/**
 * La vue du complice dessiné, permet de renseiger le surnom
 *
 * Created by ThomasHiron on 08/06/2015.
 */
public class AccompliceDrawed extends LinearLayout
{
    public AccompliceDrawed(Context context)
    {
        super(context);
    }

    public AccompliceDrawed(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* La drawedView */
        DrawedView drawedView = (DrawedView) findViewById(R.id.drawed_view);

        /* Récupération du complice */
        Cursor c = getContext().getContentResolver().query(Uri.parse(AccompliceDrawing.URL_ACCOMPLICE_DRAWINGS), null, null, null, null);

        /* Récupération de tous les points */
        if(c.moveToFirst())
        {
            do
            {
                Drawing drawing = new Drawing();
                drawing.setPoints(c.getString(c.getColumnIndex(AccompliceDrawing.ACCOMPLICE_DRAWINGS_POINTS)));
                drawing.setColor(c.getInt(c.getColumnIndex(AccompliceDrawing.ACCOMPLICE_DRAWINGS_COLOR)));

                /* On dessine */
                drawedView.draw(drawing.getPoints());
            }
            while(c.moveToNext());
        }

        c.close();
    }
}