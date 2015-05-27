package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class ColorButton extends FrameLayout
{
    private Button mButton;
    private int mColor;

    public ColorButton(Context context)
    {
        super(context);
    }

    public ColorButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        mButton = (Button) getChildAt(0);

        /* Changement du background */
        Drawable background = mButton.getBackground();
        background.setColorFilter(new LightingColorFilter(mColor, mColor));
    }

    public int getColor()
    {
        return mColor;
    }

    public void setColor(int color)
    {
        mColor = color;
    }
}
