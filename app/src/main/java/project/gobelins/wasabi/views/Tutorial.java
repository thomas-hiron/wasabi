package project.gobelins.wasabi.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import project.gobelins.wasabi.Wasabi;

/**
 * Created by ThomasHiron on 12/06/2015.
 */
public class Tutorial extends FrameLayout
{
    private Wasabi mWasabi;

    public Tutorial(Context context)
    {
        super(context);
    }

    public Tutorial(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setWasabi(Wasabi wasabi)
    {
        mWasabi = wasabi;
    }
}
