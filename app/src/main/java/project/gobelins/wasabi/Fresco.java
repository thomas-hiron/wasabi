package project.gobelins.wasabi;

import android.content.ClipData;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import project.gobelins.wasabi.drawing.DrawView;
import project.gobelins.wasabi.fragments.DrawingFragment;
import project.gobelins.wasabi.listeners.BeginFrescoDrawingListener;
import project.gobelins.wasabi.listeners.DrawingListener;
import project.gobelins.wasabi.viewPager.MyViewPager;
import project.gobelins.wasabi.viewPager.ViewPagerAdapter;

/**
 * Created by ThomasHiron on 30/04/2015.
 */
public class Fresco
{
    private FrameLayout mFrescoContainer;
    private ViewPagerAdapter mViewPagerAdapter;
    private MyViewPager mViewPager;
    private Button mDrawButton;
    private Button mPictureButton;
    private Button mSoundButton;
    private View mLastFragmentView;

    public Fresco(Wasabi wasabi)
    {
        mFrescoContainer = (FrameLayout) wasabi.findViewById(R.id.fresco_container);

        /* Récupération des boutons */
        mDrawButton = (Button) mFrescoContainer.findViewById(R.id.begin_drawing);
        mPictureButton = (Button) mFrescoContainer.findViewById(R.id.take_picture);
        mSoundButton = (Button) mFrescoContainer.findViewById(R.id.record_audio);

        /* Ajout du viewPager */
        mViewPager = (MyViewPager) mFrescoContainer.findViewById(R.id.view_pager_fresco);

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
        mDrawButton.setOnClickListener(new BeginFrescoDrawingListener(this));
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
        DrawView drawView;

        /* Récupération de la vue contenant le dessin */
        if(lastFragment.getView() != null)
        {
            mLastFragmentView = lastFragment.getView();
            drawView = (DrawView) mLastFragmentView.findViewById(R.id.draw_view);

            /* Ajout du listener sur la vue */
            drawView.setOnTouchListener(new DrawingListener(drawView, this));
        }

        /* On drag le bouton du son */
        mSoundButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                /* On démarre le drag et on cache la vue */
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.GONE);

                    return true;
                }

                return false;
            }
        });

        mViewPager.setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent)
            {
                /* Pour accepter le drag et recevoir le drop */
                if(dragEvent.getAction() == DragEvent.ACTION_DRAG_STARTED)
                    return true;
                /* Au drop, on repositionne l'objet */
                else if(dragEvent.getAction() == DragEvent.ACTION_DROP)
                {
                    /* On raffiche le bouton */
                    mSoundButton.setVisibility(View.VISIBLE);

                    /* Le layout des boutons */
                    LinearLayout buttonsContainer = (LinearLayout) mSoundButton.getParent();

                    /* On replace le bouton */
                    mSoundButton.setX(dragEvent.getX() - buttonsContainer.getX() - mSoundButton.getWidth() / 2);
                    mSoundButton.setY(dragEvent.getY() - buttonsContainer.getY() - mSoundButton.getHeight() / 2);
                }

                return false;
            }
        });
    }

    /**
     * Cache les boutons de l'interface
     */
    public void hideInterfaceButtons()
    {
        toggleInterfaceButtons(false);
    }

    /**
     * Affiche les boutons de l'interface
     */
    public void showInterfaceButtons()
    {
        toggleInterfaceButtons(true);
    }

    private void toggleInterfaceButtons(boolean show)
    {
        /* Récupération du groupe de boutons et du bouton fermer */
        FrameLayout parent = (FrameLayout) mViewPager.getParent();
        LinearLayout buttons = (LinearLayout) parent.findViewById(R.id.fresco_buttons_group);
        Button closeButton = (Button) parent.findViewById(R.id.close_fresco);

        /* Les variables de départ et d'arrivée */
        int from = show ? 0 : 1;
        int to = show ? 1 : 0;

        /* Instantiation de l'animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(from, to);
        alphaAnimation.setDuration(200);
        alphaAnimation.setFillAfter(true);

        /* On lance les animations */
        buttons.startAnimation(alphaAnimation);
        closeButton.startAnimation(alphaAnimation);
    }
}