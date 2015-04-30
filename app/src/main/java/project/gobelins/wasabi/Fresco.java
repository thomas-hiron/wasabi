package project.gobelins.wasabi;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;

import project.gobelins.wasabi.drawing.DrawView;
import project.gobelins.wasabi.fragments.DrawingFragment;
import project.gobelins.wasabi.listeners.BeginFrescoDrawing;
import project.gobelins.wasabi.listeners.DrawingListener;
import project.gobelins.wasabi.viewPager.MyViewPager;
import project.gobelins.wasabi.viewPager.ViewPagerAdapter;

/**
 * Created by ThomasHiron on 30/04/2015.
 */
public class Fresco
{
    private ViewPagerAdapter mViewPagerAdapter;
    private MyViewPager mViewPager;
    private Button mDrawButton;
    private Button mPictureButton;
    private Button mSoundButton;

    public Fresco(Wasabi wasabi)
    {
        /* Récupération des boutons */
        mDrawButton = (Button) wasabi.findViewById(R.id.begin_drawing);
        mPictureButton = (Button) wasabi.findViewById(R.id.take_picture);
        mSoundButton = (Button) wasabi.findViewById(R.id.record_audio);

        /* Ajout du viewPager */
        mViewPager = (MyViewPager) wasabi.findViewById(R.id.view_pager_fresco);

        Log.v("test", mViewPager == null ? "null" : "not null");

        /* Instanciation de l'adapter */
        mViewPagerAdapter = new ViewPagerAdapter(wasabi.getSupportFragmentManager());

        /* Ajout des fragments de chaque jour */
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());
        mViewPagerAdapter.add(DrawingFragment.newInstance());

        mViewPager.setAdapter(mViewPagerAdapter);

        /* Ajout des listeners */
        mDrawButton.setOnClickListener(new BeginFrescoDrawing(this));
    }

    /**
     * Se déplace jusqu'au dernier fragment
     */
    public void goToLastFragment()
    {
        mViewPager.setCurrentItem(mViewPagerAdapter.getCount() - 1);
    }

    /**
     * Verrouille le viewPager
     */
    public void lock()
    {
        mViewPager.lock();

    }

    /**
     * Initialise le dessin
     */
    public void initDrawing()
    {
        /* Récupération du dernier fragment */
        Fragment lastFragment = mViewPagerAdapter.getItem(mViewPagerAdapter.getCount() - 1);

        /* La vue du dessin */
        DrawView drawView = null;

        /* Récupération de la vue contenant le dessin */
        if(lastFragment.getView() != null)
        {
            drawView = (DrawView) lastFragment.getView().findViewById(R.id.draw_view);

            /* Ajout du listener sur la vue */
            drawView.setOnTouchListener(new DrawingListener(drawView));
        }
    }
}