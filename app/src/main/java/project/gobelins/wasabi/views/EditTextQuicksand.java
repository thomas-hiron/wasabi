package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ThomasHiron on 21/05/2015.
 */
public class EditTextQuicksand extends EditText
{
    public EditTextQuicksand(Context context)
    {
        super(context);
        init();
    }

    public EditTextQuicksand(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public EditTextQuicksand(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf");
        setTypeface(tf);
    }
}
