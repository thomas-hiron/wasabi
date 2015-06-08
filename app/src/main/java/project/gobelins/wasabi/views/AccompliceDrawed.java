package project.gobelins.wasabi.views;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.entities.Drawing;
import project.gobelins.wasabi.fresco.drawing.DrawedView;

/**
 * La vue du complice dessiné, permet de renseiger le surnom
 * <p/>
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
    }
}