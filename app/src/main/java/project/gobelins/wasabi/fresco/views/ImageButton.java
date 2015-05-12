package project.gobelins.wasabi.fresco.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.PicassoTarget;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.fresco.listeners.ButtonDragListener;
import project.gobelins.wasabi.interfaces.Listeners;

/**
 * Bouton qui apparaît lorsque l'enregistrement est terminé
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class ImageButton extends CircularImageView implements Listeners
{
    private String mFileName;
    private PicassoTarget mTarget;
    private Fresco mFresco;
    private Point mPoint;
    private int mId;

    private boolean mSave;

    public ImageButton(Context context)
    {
        super(context);
    }

    public ImageButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mTarget = new PicassoTarget(this);
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
        /* Initialisation du drag n' drop au clic long */
        setOnLongClickListener(new OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                /* Sorte de drag perso */
                setOnTouchListener(new ButtonDragListener(getContext()));

                return true;
            }
        });
    }
}
