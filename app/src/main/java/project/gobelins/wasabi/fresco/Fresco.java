package project.gobelins.wasabi.fresco;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fragments.DrawingFragment;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.listeners.BeginDrawListener;
import project.gobelins.wasabi.fresco.listeners.DrawingListener;
import project.gobelins.wasabi.fresco.listeners.RecordAudioListener;
import project.gobelins.wasabi.fresco.listeners.TakePictureListener;
import project.gobelins.wasabi.fresco.viewPager.ViewPagerAdapter;
import project.gobelins.wasabi.fresco.views.FrescoActionButton;
import project.gobelins.wasabi.fresco.views.FrescoViewPager;
import project.gobelins.wasabi.fresco.views.buttons.DrawButton;
import project.gobelins.wasabi.fresco.views.buttons.PictureButton;
import project.gobelins.wasabi.fresco.views.buttons.RecordButton;

/**
 * Created by ThomasHiron on 30/04/2015.
 */
public class Fresco extends FrameLayout
{
    private ViewPagerAdapter mViewPagerAdapter;
    private FrescoViewPager mViewPager;
    private FrescoActionButton mDrawButton;
    private FrescoActionButton mPictureButton;
    private FrescoActionButton mRecordButton;
    private View mLastFragmentView;

    public final static int DRAW_BUTTON = 1;
    public final static int RECORD_BUTTON = 2;
    public final static int PICTURE_BUTTON = 3;

    public Fresco(Context context)
    {
        super(context);
    }

    public Fresco(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Initialise la fresque
     *
     * @param supportFragmentManager
     */
    public void init(FragmentManager supportFragmentManager)
    {
        /* Récupération des boutons */
        mDrawButton = (DrawButton) findViewById(R.id.begin_drawing);
        mPictureButton = (PictureButton) findViewById(R.id.take_picture);
        mRecordButton = (RecordButton) findViewById(R.id.record_audio);

        /* Ajout des resources */
        mDrawButton.setResource(R.drawable.tool_pencil);
        mDrawButton.setActiveResource(R.drawable.tool_pencil_active);
        mPictureButton.setResource(R.drawable.tool_picture);
        mPictureButton.setActiveResource(R.drawable.tool_picture_active);
        mRecordButton.setResource(R.drawable.tool_record);
        mRecordButton.setActiveResource(R.drawable.tool_record_active);

        /* Ajout des listeners */
        mDrawButton.setOnClickListener(new BeginDrawListener(this, mDrawButton));
        mRecordButton.setOnClickListener(new RecordAudioListener(this, mRecordButton));
        mPictureButton.setOnClickListener(new TakePictureListener(this, mPictureButton));

        /* Ajout du viewPager */
        mViewPager = (FrescoViewPager) findViewById(R.id.view_pager_fresco);

        /* Instanciation de l'adapter */
        mViewPagerAdapter = new ViewPagerAdapter(supportFragmentManager);

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
//        mRecordButton.setOnLongClickListener(new View.OnLongClickListener()
//        {
//            @Override
//            public boolean onLongClick(View view)
//            {
//                /* On démarre le drag et on cache la vue */
//                ClipData data = ClipData.newPlainText("", "");
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//                view.startDrag(data, shadowBuilder, view, 0);
//                view.setVisibility(View.GONE);
//
//                return true;
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
//                    mRecordButton.setVisibility(View.VISIBLE);
//
//                    /* Le layout des boutons */
//                    LinearLayout buttonsContainer = (LinearLayout) mRecordButton.getParent();
//
//                    /* On replace le bouton */
//                    mRecordButton.setX(dragEvent.getX() - buttonsContainer.getX() - mRecordButton.getWidth() / 2);
//                    mRecordButton.setY(dragEvent.getY() - buttonsContainer.getY() - mRecordButton.getHeight() / 2);
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
        View view = findViewById(resourceId);

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
        if(mDrawButton.isActive())
            toggleInterfaceButtons(false, R.id.colors_buttons);
    }

    /**
     * Change l'état du bouton dessiner (background)
     *
     * @param buttonId  L'id du bouton
     * @param isDrawing S'il était en train de dessiner
     */
    public void changeButtonState(int buttonId, boolean isDrawing)
    {
        /* Le bouton d'action à changer */
        FrescoActionButton frescoActionButton = null;

        /* Les autres boutons */
        FrescoActionButton[] frescoActionButtons = null;

        /* On prend le bon bouton */
        switch(buttonId)
        {
            case DRAW_BUTTON:

                frescoActionButton = mDrawButton;
                frescoActionButtons = new FrescoActionButton[]{mRecordButton, mPictureButton};
                break;

            case RECORD_BUTTON:

                frescoActionButton = mRecordButton;
                frescoActionButtons = new FrescoActionButton[]{mDrawButton, mPictureButton};
                hideColorsButtons();
                break;

            case PICTURE_BUTTON:

                frescoActionButton = mPictureButton;
                frescoActionButtons = new FrescoActionButton[]{mDrawButton, mRecordButton};
                hideColorsButtons();
                break;
        }

        assert frescoActionButton != null;

        /* On enlève l'état actif */
        if(isDrawing)
            frescoActionButton.setImageResource(frescoActionButton.getResource());
        /* On ajout l'état actif */
        else
            frescoActionButton.setImageResource(frescoActionButton.getActiveResource());

        /* On enlève l'état actif des autres boutons */
        for(FrescoActionButton b : frescoActionButtons)
        {
            /* On change le back */
            b.setImageResource(b.getResource());

            /* On force l'état du bouton à non actif */
            b.changeState(false);
        }

    }
}