package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Field;

import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fresco.viewPager.ViewPagerAdapter;
import project.gobelins.wasabi.fresco.viewPager.ViewPagerScroller;

/**
 * Created by ThomasHiron on 16/10/2014.
 * <p/>
 * Classe héritant de ViewPager pour empêcher le scroll dans les vues
 */
public class FrescoViewPager extends ViewPager
{
    private boolean mLock;

    public FrescoViewPager(Context context)
    {
        super(context);
    }

    public FrescoViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mLock = false;

        /* Permet de changer la durée du scroll */
        try
        {
            Field scroller;
            scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            ViewPagerScroller scrollerViewPager = new ViewPagerScroller(getContext());
            scroller.set(this, scrollerViewPager);
        }
        catch(Exception e)
        {
            Log.e(Wasabi.TAG, "Error of change scroller ", e);
        }
    }

    @Override
    public ViewPagerAdapter getAdapter()
    {
        return (ViewPagerAdapter) super.getAdapter();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return mLock || super.onTouchEvent(ev);
    }

    /**
     * Verrouille le viewPager
     */
    public void lock()
    {
        mLock = true;
    }

    /**
     * Déverouille le viewPager
     */
    public void unlock()
    {
        mLock = false;
    }
}