package project.gobelins.wasabi.fresco;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Switch;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fragments.DrawingFragment;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.listeners.BeginFrescoDrawingListener;
import project.gobelins.wasabi.fresco.listeners.DrawingListener;
import project.gobelins.wasabi.fresco.listeners.RecordAudioListener;
import project.gobelins.wasabi.fresco.listeners.TakePictureListener;
import project.gobelins.wasabi.fresco.viewPager.ViewPagerAdapter;
import project.gobelins.wasabi.fresco.views.FrescoActionButton;
import project.gobelins.wasabi.fresco.views.FrescoViewPager;

/**
 * Created by ThomasHiron on 30/04/2015.
 */
public class Fresco
{
    private FrameLayout mParent;
    private FrameLayout mFrescoContainer;
    private ViewPagerAdapter mViewPagerAdapter;
    private FrescoViewPager mViewPager;
    private FrescoActionButton mDrawButton;
    private FrescoActionButton mPictureButton;
    private FrescoActionButton mRecordButton;
    private View mLastFragmentView;

    public final static int DRAW_BUTTON = 1;
    public final static int RECORD_BUTTON = 2;
    public final static int PICTURE_BUTTON = 3;

    public Fresco(Wasabi wasabi)
    {
        mFrescoContainer = (FrameLayout) wasabi.findViewById(R.id.fresco_container);

        /* Récupération des boutons */
        mDrawButton = (FrescoActionButton) mFrescoContainer.findViewById(R.id.begin_drawing);
        mPictureButton = (FrescoActionButton) mFrescoContainer.findViewById(R.id.take_picture);
        mRecordButton = (FrescoActionButton) mFrescoContainer.findViewById(R.id.record_audio);

        /* Ajout des resources */
        mDrawButton.setResource(R.drawable.tool_pencil);
        mDrawButton.setActiveResource(R.drawable.tool_pencil_active);
        mPictureButton.setResource(R.drawable.tool_picture);
        mPictureButton.setActiveResource(R.drawable.tool_picture_active);
        mRecordButton.setResource(R.drawable.tool_record);
        mRecordButton.setActiveResource(R.drawable.tool_record_active);

        /* Ajout du viewPager */
        mViewPager = (FrescoViewPager) mFrescoContainer.findViewById(R.id.view_pager_fresco);

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
        mRecordButton.setOnClickListener(new RecordAudioListener(this));
        mPictureButton.setOnClickListener(new TakePictureListener(this));

        /* Le parent */
        mParent = (FrameLayout) mViewPager.getParent();
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

//        /* On drag le bouton du son */
//        mSoundButton.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent)
//            {
//                /* On démarre le drag et on cache la vue */
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
//                {
//                    ClipData data = ClipData.newPlainText("", "");
//                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//                    view.startDrag(data, shadowBuilder, view, 0);
//                    view.setVisibility(View.GONE);
//
//                    return true;
//                }
//
//                return false;
//            }
//        });
//
//        mViewPager.setOnDragListener(new View.OnDragListener()
//        {
//            @Override
//            public boolean onDrag(View view, DragEvent dragEvent)
//            {
//                /* Pour accepter le drag et recevoir le drop */
//                if(dragEvent.getAction() == DragEvent.ACTION_DRAG_STARTED)
//                    return true;
//                /* Au drop, on repositionne l'objet */
//                else if(dragEvent.getAction() == DragEvent.ACTION_DROP)
//                {
//                    /* On raffiche le bouton */
//                    mSoundButton.setVisibility(View.VISIBLE);
//
//                    /* Le layout des boutons */
//                    LinearLayout buttonsContainer = (LinearLayout) mSoundButton.getParent();
//
//                    /* On replace le bouton */
//                    mSoundButton.setX(dragEvent.getX() - buttonsContainer.getX() - mSoundButton.getWidth() / 2);
//                    mSoundButton.setY(dragEvent.getY() - buttonsContainer.getY() - mSoundButton.getHeight() / 2);
//                }
//
//                return false;
//            }
//        });
    }

    /**
     * Cache les boutons de l'interface
     */
    public void hideInterfaceButtons()
    {
        toggleInterfaceButtons(false, R.id.fresco_buttons_group);
        toggleInterfaceButtons(false, R.id.close_fresco);
    }

    /**
     * Affiche les boutons de l'interface
     */
    public void showInterfaceButtons()
    {
        toggleInterfaceButtons(true, R.id.fresco_buttons_group);
        toggleInterfaceButtons(true, R.id.close_fresco);
    }

    /**
     * Affiche ou cache les boutons d'action
     *
     * @param show Si on doit afficher le bouton
     */
    private void toggleInterfaceButtons(boolean show, int resourceId)
    {
        /* Récupération de la ressource */
        View view = mParent.findViewById(resourceId);

        /* Les variables de départ et d'arrivée */
        int from = show ? 0 : 1;
        int to = show ? 1 : 0;

        /* Instantiation de l'animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(from, to);
        alphaAnimation.setDuration(200);
        alphaAnimation.setFillAfter(true);

        /* On lance l'aanimations */
        view.startAnimation(alphaAnimation);
    }

    /**
     * Affiche les boutons permettant de changer la couleur du dessin
     */
    public void showColorsButtons()
    {
        toggleInterfaceButtons(true, R.id.colors_buttons);
    }

    /**
     * Cache les boutons permettant de changer la couleur du dessin
     */
    public void hideColorsButtons()
    {
        toggleInterfaceButtons(false, R.id.colors_buttons);
    }

    /**
     * Change l'état du bouton dessiner (background)
     *
     * @param buttonId L'id du bouton
     * @param isDrawing S'il était en train de dessiner
     */
    public void changeButtonState(int buttonId, boolean isDrawing)
    {
        /* Le bouton d'action à changer */
        FrescoActionButton frescoActionButton = null;

        /* On prend le bon bouton */
        switch(buttonId)
        {
            case DRAW_BUTTON :

                frescoActionButton = mDrawButton;
                break;

            case RECORD_BUTTON :

                frescoActionButton = mRecordButton;
                break;

            case PICTURE_BUTTON :

                frescoActionButton = mPictureButton;
                break;
        }

        assert frescoActionButton != null;

        /* On enlève l'état actif */
        if(isDrawing)
            frescoActionButton.setImageResource(frescoActionButton.getResource());
        /* On ajout l'état actif */
        else
            frescoActionButton.setImageResource(frescoActionButton.getActiveResource());

    }
}