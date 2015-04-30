package project.gobelins.wasabi.viewPager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by ThomasHiron on 30/04/2015.
 */
public class ViewPagerScroller extends Scroller
{
    private final int mScrollDuration = 600;

    public ViewPagerScroller(Context context)
    {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator)
    {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration)
    {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy)
    {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
}
