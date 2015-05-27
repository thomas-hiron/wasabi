package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.reflect.Field;

import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fresco.Fresco;
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
    private Fresco mFresco;
    private boolean mButtonHidden;

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

        mButtonHidden = false;

        /* Listener de scroll */
        setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                /* Si page à l'arrêt */
                if(state == SCROLL_STATE_IDLE)
                {
                    /* On cache la flèche */
                    if(getCurrentItem() == getAdapter().getCount() - 1)
                    {
                        mFresco.hideGoToLastButton();
                        mButtonHidden = true;
                    }
                    else if(mButtonHidden)
                    {
                        mFresco.showGoToLastButton();
                        mButtonHidden = false;
                    }
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
        return mLock || super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return false;
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

    public void setFresco(Fresco fresco)
    {
        mFresco = fresco;
    }
}