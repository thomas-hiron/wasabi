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
public class FrescoViewPager extends ViewPager implements GestureDetector.OnGestureListener
{
    private GestureDetector mGestureDetector;
    private FlingRunnable mFlingRunnable;
    private boolean mScrolling;
    private boolean mScrollingToLeft;
    private boolean mLock;

    public FrescoViewPager(Context context)
    {
        super(context);
    }

    public FrescoViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mGestureDetector = new GestureDetector(context, this);
        mFlingRunnable = new FlingRunnable();
        mLock = false;

        /* Permet de changer la durée du scroll */
        try
        {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(getContext());
            mScroller.set(this, scroller);
        }
        catch(Exception e)
        {
            Log.e(Wasabi.TAG, "Error of change scroller ", e);
        }
    }

    public FrescoViewPager(final Wasabi wasabi)
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
    public boolean onTouchEvent(MotionEvent event)
    {
        // give all the events to the gesture detector. I'm returning true here so the viewpager doesn't
        // get any events at all, I'm sure you could adjust this to make that not true.
        if(!mLock)
            mGestureDetector.onTouchEvent(event);

        return true;
    }

    /**
     * Pas assez rapide, on stoppe le scroll
     */
    private void stopScroll()
    {
        /* Scroll à gauche */
        if(mScrollingToLeft)
        {
            // Now we know that we've hit the bound, flip the page
            if(this.getCurrentItem() > 0)
                setCurrentItem(this.getCurrentItem() - 1);
        }
        else
        {
            // Now we know that we've hit the bound, flip the page
            if(this.getCurrentItem() < (this.getAdapter().getCount() - 1))
                setCurrentItem(this.getCurrentItem() + 1);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velX, float velY)
    {
        /* Si gros scroll, on slide, sinon comportement normal */
        if(Math.abs(velX) > 6000)
            mFlingRunnable.startUsingVelocity((int) velX);
        else
            stopScroll();

        return true;
    }

    private void trackMotion(float distX)
    {
        // The following mimics the underlying calculations in ViewPager
        float scrollX = getScrollX() - distX;
        final int width = getWidth();
        final int widthWithMargin = width + this.getPageMargin();
        final float leftBound = Math.max(0, (this.getCurrentItem() - 1) * widthWithMargin);
        final float rightBound = Math.min(this.getCurrentItem() + 1, this.getAdapter().getCount() - 1) * widthWithMargin;

        if(scrollX < leftBound)
        {
            // Now we know that we've hit the bound, flip the page
            if(this.getCurrentItem() > 0)
                this.setCurrentItem(this.getCurrentItem() - 1, false);
        }
        else if(scrollX > rightBound)
        {
            // Now we know that we've hit the bound, flip the page
            if(this.getCurrentItem() < (this.getAdapter().getCount() - 1))
                this.setCurrentItem(this.getCurrentItem() + 1, false);
        }

        // Do the fake dragging
        if(mScrolling && isFakeDragging())
            this.fakeDragBy(distX);
        else if(!mScrolling)
        {
            this.beginFakeDrag();
            if(isFakeDragging())
                this.fakeDragBy(distX);
            mScrolling = true;
        }
    }

    private void endFlingMotion()
    {
        mScrolling = false;
        if(isFakeDragging())
            this.endFakeDrag();
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

    /**
     * The fling runnable which moves the view pager and tracks decay
     */
    private class FlingRunnable implements Runnable
    {
        private Scroller mScroller; // use this to store the points which will be used to create the scroll
        private int mLastFlingX;

        private FlingRunnable()
        {
            mScroller = new Scroller(getContext());
        }

        public void startUsingVelocity(int initialVel)
        {
            // there is no velocity to fling!
            if(initialVel == 0)
                return;

            removeCallbacks(this); // stop pending flings

            int initialX = initialVel < 0 ? Integer.MAX_VALUE : 0;
            mLastFlingX = initialX;
            // setup the scroller to calulate the new x positions based on the initial velocity. Impose no cap on the min/max x values.
            mScroller.fling(initialX, 0, initialVel, 0, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);

            post(this);
        }

        private void endFling()
        {
            mScroller.forceFinished(true);
            endFlingMotion();
        }

        @Override
        public void run()
        {
            final Scroller scroller = mScroller;
            boolean animationNotFinished = scroller.computeScrollOffset();
            final int x = scroller.getCurrX();
            int delta = x - mLastFlingX;

            trackMotion(delta);

            if(animationNotFinished)
            {
                mLastFlingX = x;
                post(this);
            }
            else
                endFling();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distX, float distY)
    {
        mScrollingToLeft = distX < 0;
        trackMotion(-distX);
        return false;
    }

    @Override
    public boolean onDown(MotionEvent event)
    {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event)
    {

    }

    @Override
    public void onShowPress(MotionEvent event)
    {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event)
    {
        return false;
    }
}