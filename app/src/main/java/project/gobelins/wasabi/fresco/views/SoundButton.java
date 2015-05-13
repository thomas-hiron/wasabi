package project.gobelins.wasabi.fresco.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fresco.Dustbin;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.fresco.listeners.ButtonDragListener;
import project.gobelins.wasabi.fresco.listeners.PlaySoundListener;
import project.gobelins.wasabi.interfaces.DraggableElement;
import project.gobelins.wasabi.interfaces.Listeners;

/**
 * Bouton qui apparaît lorsque l'enregistrement est terminé
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class SoundButton extends Button implements Listeners, DraggableElement
{
    private String mFileName;
    private boolean mHoveringDustbin;
    private final float SCALE_DUST = 0.7f;
    private Fresco mFresco;
    private int mId;
    private Point mPoint;
    private boolean mAddListeners;
    private boolean mSave;

    public SoundButton(Context context)
    {
        super(context);
    }

    public SoundButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mHoveringDustbin = false;
    }

    /**
     * @param fileName Le chemin du fichier
     */
    public void setFileName(String fileName)
    {
        mFileName = fileName;
    }

    /**
     * @return Le chemin du fichier
     */
    public String getFileName()
    {
        return mFileName;
    }

    public void setFresco(Fresco fresco)
    {
        mFresco = fresco;
    }

    public void setDbId(int id)
    {
        mId = id;
    }

    public int getDbId()
    {
        return mId;
    }

    public void setPoint(Point point)
    {
        mPoint = point;
    }

    public void setAddListeners(boolean addListeners)
    {
        mAddListeners = addListeners;
    }

    public void setSave(boolean save)
    {
        mSave = save;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* Ajout des listeners */
        addListeners();

        /* Placement du point */
        if(mPoint != null)
        {
            setX(mPoint.x);
            setY(mPoint.y);
        }

        /* Enregistrement */
        if(mSave)
            mFresco.saveSound(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        /* On le place au milieu si apparition */
        if(mPoint == null)
        {
            setX(Wasabi.SCREEN_WIDTH / 2 - getWidth() / 2);
            setY(Wasabi.SCREEN_HEIGHT / 2 - getHeight() / 2);
        }
    }

    /**
     * Affiche le bouton
     */
    public void appear()
    {
        if(mSave)
        {
            /* Animation du bouton */
            ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, /* Début/fin pour X/Y */
                    Animation.ABSOLUTE, Wasabi.SCREEN_WIDTH / 2, /* X */
                    Animation.ABSOLUTE, Wasabi.SCREEN_HEIGHT / 2); /* Y */
            scaleAnimation.setDuration(250);
            scaleAnimation.setInterpolator(new OvershootInterpolator());
            scaleAnimation.setStartTime(200);

            /* Début animation */
            startAnimation(scaleAnimation);
        }
    }

    /**
     * On supprime tous les listeners
     */
    public void removeListeners()
    {
        setOnClickListener(null);
        setOnLongClickListener(null);
    }

    /**
     * Ajoute les listeners drag et click
     */
    public void addListeners()
    {
        if(!mAddListeners)
            return;

        /* Au clic sur le bouton, on écoute le son */
        setOnClickListener(new PlaySoundListener());

        final SoundButton soundButton = this;

        /* Initialisation du drag n' drop au clic long */
        setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                /* Récupération des coordonnées de la poubelle */
                View rootView = getRootView();
                Dustbin dustbin = (Dustbin) rootView.findViewById(R.id.fresco_dustbin);
                try
                {
                    JSONObject coordinates = dustbin.getCoordinates();

                    /* Sorte de drag perso */
                    setOnTouchListener(new ButtonDragListener(coordinates));
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    /**
     * @return La largeur en fonction du scale
     */
    @Override
    public int getCustomWidth()
    {
        return mHoveringDustbin ? (int) (getWidth() * SCALE_DUST) : getWidth();
    }

    /**
     * @return La hauteur en fonction du scale
     */
    @Override
    public int getCustomHeight()
    {
        return mHoveringDustbin ? (int) (getHeight() * SCALE_DUST) : getHeight();
    }

    /**
     * Scale down pour préparer la suppression
     *
     * @param eventX
     * @param eventY
     */
    @Override
    public void scaleToDelete(int eventX, int eventY)
    {
        if(!mHoveringDustbin)
        {
            mHoveringDustbin = true;

            ScaleAnimation scaleAnimation = new ScaleAnimation(1, SCALE_DUST, 1, SCALE_DUST, /* Début/fin pour X/Y */
                    Animation.ABSOLUTE, eventX, /* X */
                    Animation.ABSOLUTE, eventY); /* Y */
            scaleAnimation.setDuration(250);
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setInterpolator(new OvershootInterpolator());

            /* Début animation */
            startAnimation(scaleAnimation);
        }
    }

    /**
     * Scale normal
     *
     * @param eventX
     * @param eventY
     */
    @Override
    public void scaleToNormal(int eventX, int eventY)
    {
        if(mHoveringDustbin)
        {
            mHoveringDustbin = false;

            ScaleAnimation scaleAnimation = new ScaleAnimation(SCALE_DUST, 1, SCALE_DUST, 1, /* Début/fin pour X/Y */
                    Animation.ABSOLUTE, eventX, /* X */
                    Animation.ABSOLUTE, eventY); /* Y */
            scaleAnimation.setDuration(250);
            scaleAnimation.setInterpolator(new OvershootInterpolator());

            /* Début animation */
            startAnimation(scaleAnimation);
        }
    }

    /**
     * Si l'objet est en train d'être supprimé au drop
     */
    @Override
    public boolean isDeleting()
    {
        return mHoveringDustbin;
    }

    /**
     * Supprime l'élément
     *
     * @param eventX
     * @param eventY
     */
    @Override
    public void delete(float eventX, float eventY)
    {
        mHoveringDustbin = false;

        ScaleAnimation scaleAnimation = new ScaleAnimation(SCALE_DUST, 0, SCALE_DUST, 0, /* Début/fin pour X/Y */
                Animation.ABSOLUTE, eventX, /* X */
                Animation.ABSOLUTE, eventY); /* Y */
        scaleAnimation.setDuration(250);
        scaleAnimation.setInterpolator(new LinearInterpolator());

        /* Début animation */
        startAnimation(scaleAnimation);

        final SoundButton view = this;

        /* Listener de fin */
        scaleAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                mFresco.deleteSound(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }
}
