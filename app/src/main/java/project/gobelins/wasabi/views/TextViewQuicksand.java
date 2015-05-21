package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ThomasHiron on 21/05/2015.
 */
public class TextViewQuicksand extends TextView
{
    public TextViewQuicksand(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewQuicksand(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public TextViewQuicksand(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf");
        setTypeface(tf);
    }
}
