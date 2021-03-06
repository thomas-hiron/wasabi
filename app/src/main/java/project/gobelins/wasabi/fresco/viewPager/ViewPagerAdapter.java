package project.gobelins.wasabi.fresco.viewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import project.gobelins.wasabi.fragments.FrescoFragment;

/**
 * Created by ThomasHiron on 16/10/2014.
 * <p/>
 * Vue pager perso
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter
{
    ArrayList<FrescoFragment> mList = null;

    public ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
        mList = new ArrayList<FrescoFragment>();
    }

    /**
     * Ajoute une vue au tableau
     *
     * @param fragment Le fragment à ajouter
     */
    public void add(FrescoFragment fragment)
    {
        mList.add(fragment);
    }

    @Override
    public FrescoFragment getItem(int i)
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