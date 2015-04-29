package project.gobelins.wasabi.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import project.gobelins.wasabi.Wasabi;

/**
 * Created by ThomasHiron on 16/10/2014.
 * <p/>
 * Classe héritant de ViewPager pour empêcher le scroll dans les vues
 */
public class MyViewPager extends ViewPager
{
    public MyViewPager(Context context)
    {
        super(context);
    }

    public MyViewPager(final Wasabi wasabi)
    {
        super(wasabi);

        /* Listener de scroll */
        setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i2)
            {

            }

            @Override
            public void onPageSelected(int i)
            {

            }

            @Override
            public void onPageScrollStateChanged(int i)
            {
                /* On lance la notification */
                if(i == SCROLL_STATE_IDLE)
                {

                }
            }
        });
    }

    @Override
    public ViewPagerAdapter getAdapter()
    {
        return (ViewPagerAdapter) super.getAdapter();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return true;
    }
}