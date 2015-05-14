package project.gobelins.wasabi.fresco.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fresco.Dustbin;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.PicassoTarget;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.fresco.listeners.ButtonDragListener;
import project.gobelins.wasabi.interfaces.DraggableElement;
import project.gobelins.wasabi.interfaces.Listeners;

/**
 * Bouton qui apparaît lorsque l'enregistrement est terminé
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class ImageButton extends CircularImageView implements Listeners, DraggableElement
{
    private String mFileName;
    private PicassoTarget mTarget;
    private Fresco mFresco;
    private Point mPoint;
    private int mId;

    private final float SCALE_DUST = 0.5f;

    private boolean mSave;
    private boolean mHoveringDustbin;
    private boolean mAddListeners;

    public ImageButton(Context context)
    {
        super(context);
    }

    public ImageButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mTarget = new PicassoTarget(this);
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
     * @param id L'id de la BDD
     */
    public void setId(int id)
    {
        mId = id;
    }

    public String getFileName()
    {
        return mFileName;
    }

    public void setDbId(int mId)
    {
        this.mId = mId;
    }

    public int getDbId()
    {
        return mId;
    }

    public Point getPoint()
    {
        return mPoint;
    }

    /**
     * @param fresco La fresque
     */
    public void setFresco(Fresco fresco)
    {
        mFresco = fresco;
    }

    public void setSave(boolean save)
    {
        this.mSave = save;
    }

    public void setPoint(Point point)
    {
        mPoint = point;
    }

    public void setAddListeners(boolean addListeners)
    {
        mAddListeners = addListeners;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* On charge l'image avec animation */
        mTarget.setSave(mSave);
        Picasso.with(getContext()).load(Uri.parse("file:" + mFileName)).into(mTarget);

        /* Ajout des listeners */
        addListeners();

        /* Placement du point */
        if(mPoint != null)
        {
            setX(mPoint.x);
            setY(mPoint.y);
        }

        /* Enregistrement de l'image */
        if(mSave)
            mFresco.saveImage(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        /* Vue chargée, si déjà enregistrée et au milieu, on met à jour */
        if(mPoint == null && !mSave)
        {
            setX(Wasabi.SCREEN_WIDTH / 2 - getWidth() / 2);
            setY(Wasabi.SCREEN_HEIGHT / 2 - getHeight() / 2);

            /* Mise à jour */
            mFresco.updateImage(this);
        }

    }

    /**
     * On supprime tous les listeners
     */
    public void removeListeners()
    {
        setOnLongClickListener(null);
    }

    /**
     * Ajoute les listeners drag et click
     */
    public void addListeners()
    {
        if(!mAddListeners)
            return;

        /* Initialisation du drag n' drop au clic long */
        setOnLongClickListener(new OnLongClickListener()
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
     * On scale l'image pour la faire coller à la poubelle
     *
     * @param eventX
     * @param eventY
     */
    public void scaleToDelete(final int eventX, final int eventY)
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
     * Leave poubelle, scale normal
     *
     * @param eventX
     * @param eventY
     */
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
     * @return width perso
     */
    public int getCustomWidth()
    {
        return mHoveringDustbin ? (int) (getWidth() * SCALE_DUST) : getWidth();
    }

    /**
     * @return height perso
     */
    public int getCustomHeight()
    {
        return mHoveringDustbin ? (int) (getHeight() * SCALE_DUST) : getHeight();
    }

    public boolean isDeleting()
    {
        return mHoveringDustbin;
    }

    /**
     * Suppression de l'image
     *
     * @param eventX
     * @param eventY
     */
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

        final ImageButton view = this;

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
                mFresco.deleteImage(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }
}
