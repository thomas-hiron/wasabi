package project.gobelins.wasabi.viewPager;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import project.gobelins.wasabi.fragments.NotificationFragment;

/**
 * Created by ThomasHiron on 16/10/2014.
 *
 * Vue pager perso
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter
{
    ArrayList<Fragment> mList = null;

    public ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
        mList = new ArrayList<Fragment>();
    }

    /**
     * Ajoute une vue au tableau
     *
     * @param fragment Le fragment à ajouter
     */
    public void add(Fragment fragment)
    {
        mList.add(fragment);
    }

    @Override
    public Fragment getItem(int i)
    {
        return mList.get(i);
    }

    @Override
    public int getCount()
    {
        return mList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object)
    {
        /* Si l'objet n'existe pas ou qu'il y a un seul élément (home) -> On recharge */
        if(mList.indexOf(object) == -1 || getCount() == 1)
            return POSITION_NONE;
        else
            return POSITION_UNCHANGED;
    }
}