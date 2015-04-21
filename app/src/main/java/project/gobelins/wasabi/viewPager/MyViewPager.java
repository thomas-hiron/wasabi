package project.gobelins.wasabi.viewPager;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import project.gobelins.wasabi.Wasabi;

/**
 * Created by ThomasHiron on 16/10/2014.
 *
 * Classe héritant de ViewPager pour empêcher le scroll dans les vues
 */
public class MyViewPager extends ViewPager
{
    private boolean mRemoveLast = false;

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
                /* Si page à l'arrêt et on supprime le dernier */
                if(i == SCROLL_STATE_IDLE && mRemoveLast)
                {
                    /* Adapter */
                    ViewPagerAdapter viewPagerAdapter = getAdapter();

                    /* On supprime le dernier élément */
                    viewPagerAdapter.removeLast().notifyDataSetChanged();

                    /* Pour ne pas repasser dans la boucle */
                    mRemoveLast = false;

                    /* L'adapter du viewPager */
                    ViewPagerAdapter adapter = getAdapter();
                }
            }
        });
    }

    @Override
    public ViewPagerAdapter getAdapter()
    {
        return (ViewPagerAdapter) super.getAdapter();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev)
//    {
//        return true;
//    }
}