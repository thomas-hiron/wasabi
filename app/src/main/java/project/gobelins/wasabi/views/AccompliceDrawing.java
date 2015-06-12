package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.TransitionDrawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.drawing.DrawedView;
import project.gobelins.wasabi.fresco.listeners.DrawingListener;
import project.gobelins.wasabi.fresco.views.buttons.CancelButton;
import project.gobelins.wasabi.fresco.views.buttons.DrawButton;

/**
 * Created by ThomasHiron on 08/06/2015.
 */
public class AccompliceDrawing extends FrameLayout implements View.OnClickListener
{
    private Wasabi mWasabi;
    private ImageView mValidate;
    private DrawedView mDrawedView;
    private CancelButton mCancelButton;

    public AccompliceDrawing(Context context)
    {
        super(context);
    }

    public AccompliceDrawing(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setWasabi(Wasabi wasabi)
    {
        mWasabi = wasabi;
    }

    /**
     * Initialise la vue
     */
    public void init()
    {
        /* Les vues */
        DrawView drawView = (DrawView) findViewById(R.id.draw_view);
        mDrawedView = (DrawedView) findViewById(R.id.drawed_view);

        drawView.setOnTouchListener(new DrawingListener(drawView, mDrawedView, this));

        /* Bouton valider */
        mValidate = (ImageView) findViewById(R.id.identikit_completed);

        /* Listener sur le bouton terminer */
        mValidate.setOnClickListener(this);

        /* Activation du bouton de dessin */
        DrawButton drawButton = (DrawButton) findViewById(R.id.draw_button);

        /* On met l'état actif */
        TransitionDrawable transition = (TransitionDrawable) drawButton.getBackground();
        transition.startTransition(0);

         /* Clic sur le bouton annuler */
        mCancelButton = (CancelButton) findViewById(R.id.cancel_last_draw);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        /* Enregistrement du complice */
        if(view.getId() == R.id.identikit_completed)
            identikitCompleted();
        else if(view.getId() == R.id.cancel_last_draw)
            cancelLastDraw();
    }

    /**
     * Enregistre le portrait robot
     */
    private void identikitCompleted()
    {
        /* Enregistrement du complice */
        mDrawedView.setDrawingCacheEnabled(true);
        Bitmap b = mDrawedView.getDrawingCache();
        try
        {
            String storageString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/wasabi";

            /* Instanciation pour tester si dossier existant */
            File file = new File(storageString);
            if(!file.exists())
                file.mkdirs();

            file = new File(file.getAbsolutePath(), "identikit.png");

            /* Compression */
            b.compress(Bitmap.CompressFormat.PNG, 95, new FileOutputStream(file));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        /* Ajout de la nouvelle vue */
        mWasabi.removeDrawingAccompliceView();
        mWasabi.addDrawedAccompliceView();
    }

    /* Supprime le dernier dessin */
    private void cancelLastDraw()
    {
        mDrawedView.cancelLastDraw();

        /* On teste si on doit cacher la flèche */
        if(mDrawedView.getCount() == 0)
        {
            hideCancelButton();
            hideNextStep();
        }
    }

    /**
     * Affiche la flèche de retour
     */
    private void showCancelButton()
    {
        if(mDrawedView.getCount() <= 1)
        {
            mCancelButton.setVisibility(VISIBLE);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(300);

            mCancelButton.startAnimation(alphaAnimation);
            mCancelButton.setOnClickListener(this);
        }
    }

    /**
     * Cache la flèche de retour
     */
    private void hideCancelButton()
    {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);

        mCancelButton.startAnimation(alphaAnimation);
        mCancelButton.setOnClickListener(null);
    }

    /**
     * Enregistre le dessin
     */
    public void showNextStep()
    {
        /* On affiche le bouton valider */
        if(mDrawedView.getCount() <= 1)
        {
            mValidate.setVisibility(VISIBLE);

            /* Animation */
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(300);

            mValidate.startAnimation(alphaAnimation);
            mValidate.setOnClickListener(this);
        }
    }

    /**
     * Cache le bouton valider
     */
    private void hideNextStep()
    {
        /* Animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);

        mValidate.startAnimation(alphaAnimation);
        mValidate.setOnClickListener(null);
    }

    /**
     * Up, affiche le bouton valider et la flèche de retour
     */
    public void actionUp()
    {
        showCancelButton();
        showNextStep();
    }
}
