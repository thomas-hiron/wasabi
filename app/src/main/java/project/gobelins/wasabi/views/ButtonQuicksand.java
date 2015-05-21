package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by ThomasHiron on 21/05/2015.
 */
public class ButtonQuicksand extends Button
{
    public ButtonQuicksand(Context context)
    {
        super(context);
        init();
    }

    public ButtonQuicksand(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ButtonQuicksand(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Bold.ttf");
        setTypeface(tf);
    }
}
