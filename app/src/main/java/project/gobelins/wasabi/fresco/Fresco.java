package project.gobelins.wasabi.fresco;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.entities.Entity;
import project.gobelins.wasabi.fragments.FrescoFragment;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.drawing.DrawedView;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.fresco.listeners.AlphaAnimationListener;
import project.gobelins.wasabi.fresco.listeners.BeginDrawListener;
import project.gobelins.wasabi.fresco.listeners.CancelLastDrawListener;
import project.gobelins.wasabi.fresco.listeners.DrawingListener;
import project.gobelins.wasabi.fresco.listeners.RecordAudioListener;
import project.gobelins.wasabi.fresco.listeners.TakePictureListener;
import project.gobelins.wasabi.fresco.viewPager.ViewPagerAdapter;
import project.gobelins.wasabi.fresco.views.FrescoActionButton;
import project.gobelins.wasabi.fresco.views.FrescoViewPager;
import project.gobelins.wasabi.fresco.views.ImageButton;
import project.gobelins.wasabi.fresco.views.SoundButton;
import project.gobelins.wasabi.fresco.views.buttons.CancelButton;
import project.gobelins.wasabi.fresco.views.buttons.DrawButton;
import project.gobelins.wasabi.fresco.views.buttons.PictureButton;
import project.gobelins.wasabi.fresco.views.buttons.RecordButton;
import project.gobelins.wasabi.interfaces.OnCanceledListener;
import project.gobelins.wasabi.interfaces.OnPictureListener;
import project.gobelins.wasabi.interfaces.OnToggleCancelArrowListener;
import project.gobelins.wasabi.utils.DateFormater;

/**
 * Classe qui gère toute la fresque
 * <p/>
 * Created by ThomasHiron on 30/04/2015.
 */
public class Fresco extends FrameLayout implements OnToggleCancelArrowListener, OnCanceledListener, OnPictureListener
{
    public final static int DRAW_BUTTON = 1;
    public final static int RECORD_BUTTON = 2;
    public final static int PICTURE_BUTTON = 3;
    public final static int ANIMATION_DURATION = 200;
    public final static float DRAWED_VIEW_MIN_OPACITY = 0.3f;

    private ViewPagerAdapter mViewPagerAdapter;
    private FrescoViewPager mViewPager;

    private FrescoActionButton mDrawButton;
    private FrescoActionButton mPictureButton;
    private FrescoActionButton mRecordButton;
    private CancelButton mCancelButton;

    private DrawView mDrawView;
    private DrawedView mDrawedView;
    private FrameLayout mSoundView;
    private FrameLayout mImagesView;
    private MediaPlayer mMediaPlayer;
    private OnPictureListener mPictureListener;
    private DrawingsManager mDrawingsManager;
    private ImagesManager mImagesManager;

    public Fresco(Context context)
    {
        super(context);
    }

    public Fresco(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mDrawingsManager = new DrawingsManager(getContext().getContentResolver());
        mImagesManager = new ImagesManager(getContext().getContentResolver());
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* Récupération des boutons */
        mDrawButton = (DrawButton) findViewById(R.id.begin_drawing);
        mPictureButton = (PictureButton) findViewById(R.id.take_picture);
        mRecordButton = (RecordButton) findViewById(R.id.record_audio);
        mCancelButton = (CancelButton) findViewById(R.id.cancel_last_draw);

        /* Ajout des listeners */
        mDrawButton.setOnClickListener(new BeginDrawListener(this, mDrawButton));
        mRecordButton.setOnClickListener(new RecordAudioListener(this, mRecordButton));
        mPictureButton.setOnClickListener(new TakePictureListener(this));
    }

    /**
     * Initialise le viewPager
     *
     * @param supportFragmentManager Le supportFragmentManager
     */
    public void initViewPager(FragmentManager supportFragmentManager)
    {
        /* Ajout du viewPager */
        mViewPager = (FrescoViewPager) findViewById(R.id.view_pager_fresco);

        /* Instanciation de l'adapter */
        mViewPagerAdapter = new ViewPagerAdapter(supportFragmentManager);

        Date today = DateFormater.getStrictToday();
        boolean isLastFragment = false;

        /* Les dessins */
        HashMap<Date, ArrayList<Entity>> drawings = mDrawingsManager.getDrawings();
        /* Les images */
        HashMap<Date, ArrayList<Entity>> images = mImagesManager.getImages();
        /* Tous les éléments */
        HashMap<Date, ArrayList<Entity>> entities = new HashMap<>();

        /* Ajout du tableau de dessin */
        entities.putAll(drawings);

        /* Parcourt de toutes les images */
        for(Map.Entry<Date, ArrayList<Entity>> dateEntities : images.entrySet())
        {
            /* Récupération de la date correspondante du tableau final */
            ArrayList<Entity> currentDateEntities = entities.get(dateEntities.getKey());
            /* Instanciation si null */
            if(currentDateEntities == null)
                currentDateEntities = new ArrayList<>();

            /* Ajout de toutes les images */
            currentDateEntities.addAll(dateEntities.getValue());

            /* On remet la liste */
            entities.put(dateEntities.getKey(), currentDateEntities);
        }

        /* On trie le tableau */
        SortedSet<Date> orderedDates = new TreeSet<>(entities.keySet());

        /* Création d'un fragment pour chaque date */
        for(Date date : orderedDates)
        {
            /* Si dernier fragment */
            isLastFragment = today.compareTo(date) == 0;

            /* Instanciation */
            FrescoFragment frescoFragment = FrescoFragment.newInstance(
                    entities.get(date),
                    date,
                    isLastFragment
            );
            /* Ajout au viewPager */
            mViewPagerAdapter.add(frescoFragment);
        }

        /* On test si fragment à la date du jour existe */
        if(!isLastFragment)
        {
            FrescoFragment frescoFragment = FrescoFragment.newInstance(today, true);
            mViewPagerAdapter.add(frescoFragment);
        }

        /* Ajout de l'adapter */
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    /**
     * Se déplace jusqu'au dernier fragment avec scmooth scroll par défaut
     */
    public void goToLastFragment()
    {
        goToLastFragment(true);
    }

    /**
     * Se déplace jusqu'au dernier fragment
     *
     * @param smoothScroll Si on doit effectuer un smooth scroll
     */
    public void goToLastFragment(boolean smoothScroll)
    {
        mViewPager.setCurrentItem(mViewPagerAdapter.getCount() - 1, smoothScroll);
    }

    /**
     * Verrouille le viewPager
     */
    public void lock()
    {
        mViewPager.lock();
    }

    /**
     * Déverrouille le viewPager
     */
    public void unlock()
    {
        mViewPager.unlock();
    }

    /**
     * Initialise le dessin (au clic sur le bouton dessiner)
     */
    public void initDrawing()
    {
        /* Initialise la zone de dessin */
        initDrawedView();

        assert mDrawView != null;
        assert mDrawedView != null;

        /* Ajout du listener sur la vue */
        mDrawView.setOnTouchListener(new DrawingListener(mDrawView, mDrawedView, this));

        /* Ajout du listener sur annuler */
        mCancelButton.setOnClickListener(new CancelLastDrawListener(this));
    }

    /**
     * Initialise le son
     */
    public void initSounds()
    {
        if(mSoundView == null && getLastFragment().getView() != null)
            mSoundView = (FrameLayout) getLastFragment().getView().findViewById(R.id.sounds_view);

        /* Pour récupérer la vue du dessin si non initialisée */
        initDrawedView();
        initImagesView();
    }

    /**
     * Initialise les images
     */
    public void initImages()
    {
        initImagesView();
    }

    /**
     * Initialise la zone de dessin
     */
    public void initDrawedView()
    {
        /* Récupération de la vue contenant le dessin */
        FrescoFragment lastFragment = getLastFragment();
        if(lastFragment.getView() != null && mDrawView == null && mDrawedView == null)
        {
            mDrawView = (DrawView) lastFragment.getView().findViewById(R.id.draw_view);
            mDrawedView = (DrawedView) lastFragment.getView().findViewById(R.id.drawed_view);
            mDrawedView.setOnToggleCancelArrowListener(this);
        }
    }

    /**
     * Initialise la vue des images
     */
    public void initImagesView()
    {
        if(mImagesView == null && getLastFragment().getView() != null)
            mImagesView = (FrameLayout) getLastFragment().getView().findViewById(R.id.pictures_view);
    }

    /**
     * Cache les boutons de l'interface
     */
    public void hideInterfaceButtons()
    {
        toggleViewOpacity(false, R.id.fresco_buttons_group);
        toggleViewOpacity(false, R.id.close_fresco);
    }

    /**
     * Affiche les boutons de l'interface
     */
    public void showInterfaceButtons()
    {
        toggleViewOpacity(true, R.id.fresco_buttons_group);
        toggleViewOpacity(true, R.id.close_fresco);
    }

    /**
     * Affiche ou cache les boutons d'action (de 0 à 1 ou inversement)
     *
     * @param show       Si on doit afficher le bouton
     * @param resourceId L'id de la resource
     */
    private void toggleViewOpacity(boolean show, int resourceId)
    {
        /* Les variables de départ et d'arrivée */
        float from = show ? 0 : 1;
        float to = show ? 1 : 0;

        toggleViewOpacity(resourceId, from, to);
    }

    /**
     * Animation avec des valeurs personnalisés
     *
     * @param resourceId L'id de la resource
     * @param from       La valeur de départ
     * @param to         La valeur finale
     */
    private void toggleViewOpacity(int resourceId, float from, float to)
    {
        /* Récupération de la ressource */
        View view = findViewById(resourceId);

        toggleViewOpacity(view, from, to);
    }

    /**
     * Animation avec des valeurs personnalisés
     *
     * @param view L'id de la resource
     * @param from La valeur de départ
     * @param to   La valeur finale
     */
    private void toggleViewOpacity(View view, float from, float to)
    {
        /* On affiche la vue si cachée */
        if(view.getVisibility() == View.INVISIBLE)
            view.setVisibility(VISIBLE);

        /* Instantiation de l'animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(from, to);
        alphaAnimation.setDuration(ANIMATION_DURATION);
        alphaAnimation.setFillAfter(to != 0 && to != 1);

        /* On lance l'animation */
        alphaAnimation.setAnimationListener(new AlphaAnimationListener(view, to));
        view.startAnimation(alphaAnimation);
    }

    /**
     * Affiche les boutons permettant de changer la couleur du dessin
     */
    public void showColorsButtons()
    {
        toggleViewOpacity(true, R.id.colors_buttons);
    }

    /**
     * Cache les boutons permettant de changer la couleur du dessin
     */
    public void hideColorsButtons()
    {
        if(mDrawButton.isActive())
            toggleViewOpacity(false, R.id.colors_buttons);
    }

    /**
     * Affiche la flèche pour annuler
     */
    public void showCancelButton()
    {
        /* Si le précédent état de la flèche est actif */
        if(mCancelButton.isActive())
            toggleViewOpacity(true, mCancelButton.getId());
    }

    /**
     * Cache le bouton annuler le dernier dessin
     */
    public void hideCancelButton()
    {
        if(mCancelButton.isActive())
            toggleViewOpacity(false, mCancelButton.getId());
    }

    /**
     * Affiche la vue pour l'enregistrement
     */
    public void showRecordView()
    {
        toggleViewOpacity(true, R.id.start_recording_container);

        /* On baisse l'opacité de la zone de dessin et du son */
        if(mDrawedView != null)
            toggleViewOpacity(mDrawedView, 1f, DRAWED_VIEW_MIN_OPACITY);
        if(mSoundView != null)
            toggleViewOpacity(mSoundView, 1f, DRAWED_VIEW_MIN_OPACITY);
        if(mImagesView != null)
            toggleViewOpacity(mImagesView, 1f, DRAWED_VIEW_MIN_OPACITY);
    }

    /**
     * Cache la vue pour l'enregistrement
     */
    public void hideRecordView()
    {
        toggleViewOpacity(false, R.id.start_recording_container);

        /* On remonte l'opacité de la zone de dessin et du son */
        if(mDrawedView != null)
            toggleViewOpacity(mDrawedView, DRAWED_VIEW_MIN_OPACITY, 1f);
        if(mSoundView != null)
            toggleViewOpacity(mSoundView, DRAWED_VIEW_MIN_OPACITY, 1f);
        if(mImagesView != null)
            toggleViewOpacity(mImagesView, DRAWED_VIEW_MIN_OPACITY, 1f);
    }

    /**
     * Affiche la zone dessinée
     */
    public void showDrawedView()
    {
        toggleViewOpacity(true, R.id.drawed_view);
    }

    /**
     * Cache la zone dessinée
     */
    public void hideSoundsView()
    {
        toggleViewOpacity(R.id.sounds_view, DRAWED_VIEW_MIN_OPACITY, 0);
    }

    /**
     * Affiche la zone des boutons de son
     */
    public void showSoundsView()
    {
        toggleViewOpacity(true, R.id.sounds_view);
    }

    /**
     * Cache la zone dessinée
     */
    public void hideImagesView()
    {
        toggleViewOpacity(R.id.pictures_view, DRAWED_VIEW_MIN_OPACITY, 0);
    }

    /**
     * Affiche la zone des boutons des images
     */
    public void showImagesView()
    {
        toggleViewOpacity(true, R.id.pictures_view);
    }

    /**
     * Cache la zone des boutons de son
     */
    public void hideDrawedView()
    {
        toggleViewOpacity(R.id.drawed_view, DRAWED_VIEW_MIN_OPACITY, 0);
    }

    /**
     * Affiche le dégradé d'enregistrement
     */
    public void showRecordingGradient()
    {
        toggleViewOpacity(true, R.id.record_gradient);
    }

    /**
     * Cache le dégradé d'enregistrement
     */
    public void hideRecordingGradient()
    {
        toggleViewOpacity(false, R.id.record_gradient);
    }

    /**
     * Affiche la poubelle
     */
    public void showDustbin()
    {
        toggleViewOpacity(true, R.id.fresco_dustbin);
    }

    /**
     * Cache la poubelle
     */
    public void hideDustbin()
    {
        toggleViewOpacity(false, R.id.fresco_dustbin);
    }

    /**
     * On provoque le clic sur le bouton enregistrer pour le désactiver
     */
    public void disableRecordButton()
    {
        if(mRecordButton.isActive())
            mRecordButton.performClick();
    }

    /**
     * On provoque le clic sur le bouton dessiner pour le désactiver
     */
    public void disableDrawing()
    {
        if(mDrawButton.isActive())
            mDrawButton.performClick();
    }

    /**
     * Supprime les listeners pour dessiner et effacer
     */
    public void removeDrawingListeners()
    {
        mDrawView.setOnTouchListener(null);
        mCancelButton.setOnClickListener(null);
    }

    /**
     * Change l'état du bouton dessiner (background)
     *
     * @param buttonId L'id du bouton
     * @param isActive S'il était en train de dessiner
     */
    public void changeButtonState(int buttonId, boolean isActive)
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
                break;

            case PICTURE_BUTTON:

                frescoActionButton = mPictureButton;
                frescoActionButtons = new FrescoActionButton[]{mDrawButton, mRecordButton};
                break;
        }

        assert frescoActionButton != null;

        /* Animation du background */
        TransitionDrawable transition = (TransitionDrawable) frescoActionButton.getBackground();

        /* On enlève l'état actif */
        if(!isActive)
            transition.reverseTransition(ANIMATION_DURATION);
        /* On ajout l'état actif */
        else
            transition.startTransition(ANIMATION_DURATION);

        /* On enlève l'état actif des autres boutons */
        for(FrescoActionButton b : frescoActionButtons)
        {
            /* Si bouton actif, on le désactive */
            if(b.isActive())
            {
                /* Animation */
                TransitionDrawable otherTransition = (TransitionDrawable) b.getBackground();

                /* On change le back */
                otherTransition.reverseTransition(ANIMATION_DURATION);

                /* On force l'état du bouton à non actif */
                b.changeState(false);
            }
        }

        /* Si aucun bouton actif, on désactive le lock */
        boolean buttonActive = mDrawButton.isActive() || mRecordButton.isActive() || mPictureButton.isActive();

        if(!buttonActive)
            unlock();
    }

    /**
     * On fait apparaître ou disparaitre la flèche pour annuler
     *
     * @param show Si on doit afficher
     */
    @Override
    public void toggleCancelArrowListener(boolean show)
    {
        /* On l'affiche si non actif */
        if(show && !mCancelButton.isActive())
            toggleViewOpacity(true, mCancelButton.getId());
        else if(!show && mCancelButton.isActive())
            toggleViewOpacity(false, mCancelButton.getId());

        /* On change l'état de la flèche */
        mCancelButton.isActive(show);
    }

    /**
     * On fait apparaître ou disparaitre la flèche pour annuler
     */
    public void toggleCancelArrowListener()
    {
        int nbCurbs = mDrawedView.getCount();
        toggleCancelArrowListener(nbCurbs > 0);
    }

    /**
     * On a cliqué sur le bouton annulé
     */
    @Override
    public void onCanceled()
    {
        mDrawedView.cancelLastDraw();
        mDrawingsManager.deleteLastDraw();
    }

    /**
     * @return Le dernier fragment
     */
    public FrescoFragment getLastFragment()
    {
        return mViewPagerAdapter.getItem(mViewPagerAdapter.getCount() - 1);
    }

    /**
     * @return Le MediaPlayer
     */
    public MediaPlayer getMediaPlayer()
    {
        /* Instanciation que si besoin */
        if(mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();

        return mMediaPlayer;
    }

    /**
     * @return La vue de dessin
     */
    public DrawView getDrawView()
    {
        return mDrawView;
    }

    /**
     * Ajoute un nouveau son
     *
     * @param fileName Le nom du fichier enregistré
     */
    public void addNewSound(String fileName)
    {
        /* L'inflater */
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /* La vue parent */
        View fragmentView = getLastFragment().getView();
        FrameLayout soundsContainer = null;
        if(fragmentView != null)
            soundsContainer = (FrameLayout) fragmentView.findViewById(R.id.sounds_view);

        /* Inflation de la vue */
        SoundButton soundButton = (SoundButton) inflater.inflate(R.layout.fresco_sound, soundsContainer, false);

        /* Ajout du chemin */
        soundButton.setFileName(fileName);

        /* Ajout de la vue */
        if(soundsContainer != null)
            soundsContainer.addView(soundButton);
    }

    /**
     * Ajoute une nouvelle image dans le dernier fragment
     *
     * @param imageUrl L'image
     */
    public void addNewPicture(String imageUrl)
    {
        /* La vue parent */
        View fragmentView = getLastFragment().getView();
        FrameLayout imagesContainer = null;
        if(fragmentView != null)
            imagesContainer = (FrameLayout) getLastFragment().getView().findViewById(R.id.pictures_view);

        /* Ajout de la vue */
        if(imagesContainer != null)
            addNewPicture(imagesContainer, imageUrl);
    }

    /**
     * Ajoute une nouvelle image dans le container spécifié
     *
     * @param container Le conteneur
     * @param imageUrl  L'image
     */
    public void addNewPicture(FrameLayout container, String imageUrl)
    {
        addNewPicture(container, imageUrl, true);
    }

    /**
     * Ajoute une nouvelle image dans le container spécifié, avec un paramètre pour spécifier si enregistrement
     *
     * @param container Le conteneur
     * @param imageUrl  L'image
     * @param save      Si on enregistre
     */
    public void addNewPicture(FrameLayout container, String imageUrl, boolean save)
    {
        addNewPicture(container, imageUrl, 0, null, save, true);
    }

    /**
     * Ajoute une nouvelle image dans le container spécifié, avec un paramètre pour spécifier si enregistrement
     *
     * @param container Le conteneur
     * @param imageUrl  L'image
     * @param id        L'id
     * @param point     Le point
     * @param save      Si on enregistre
     */
    public void addNewPicture(FrameLayout container, String imageUrl, int id, Point point, boolean save, boolean addListeners)
    {
        /* L'inflater */
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /* Inflation de la vue */
        ImageButton imageButton = (ImageButton) inflater.inflate(R.layout.fresco_picture, container, false);

        /* Ajout du chemin */
        imageButton.setFileName(imageUrl);
        imageButton.setFresco(this);
        imageButton.setSave(save);
        imageButton.setPoint(point);
        imageButton.setAddListeners(addListeners);

        if(id != 0)
            imageButton.setDbId(id);

        /* Ajout de la vue */
        if(container != null)
            container.addView(imageButton);
    }

    /**
     * Ajout du listener pour la prise de photo
     *
     * @param listener Le listener
     */
    public void setPictureListener(OnPictureListener listener)
    {
        mPictureListener = listener;
    }

    /**
     * On a cliqué sur le bouton prendre une photo
     */
    @Override
    public void onTakePicture()
    {
        mPictureListener.onTakePicture();
    }

    /**
     * Enregistre tous les points
     *
     * @param points Le tableau de points
     */
    public void saveDrawing(ArrayList<Point> points)
    {
        mDrawingsManager.saveDrawing(points);
    }

    /**
     * On enregistre l'image
     *
     * @param imageButton L'image
     */
    public void saveImage(ImageButton imageButton)
    {
        /* Enregistrement et renseignement de l'id */
        int id = mImagesManager.saveImage(imageButton.getFileName());
        imageButton.setId(id);
    }

    /**
     * Met à jour la position d'une image
     *
     * @param imageButton L'image
     */
    public void updateImage(ImageButton imageButton)
    {
        Point point = new Point();
        point.set(imageButton.getX(), imageButton.getY());

        /* Mise à jour des coordonnées */
        mImagesManager.updateImage(point, imageButton.getDbId());
    }

    public void deleteImage(ImageButton view)
    {
        if(mImagesView == null)
            initImagesView();

        mImagesView.removeView(view);
    }
}