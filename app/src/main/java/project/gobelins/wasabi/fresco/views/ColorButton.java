package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class ColorButton extends Button
{
    private LightingColorFilter mColor;

    public ColorButton(Context context)
    {
        super(context);
    }

    public ColorButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LightingColorFilter getColor()
    {
        return mColor;
    }

    public void setColor(int color)
    {
        mColor = new LightingColorFilter(color, color);

        /* Changement du background */
        Drawable background = getBackground();
        background.setColorFilter(new LightingColorFilter(color, color));
    }
}
