package project.gobelins.wasabi.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.drawing.DrawedView;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.fresco.listeners.DrawingListener;
import project.gobelins.wasabi.fresco.views.buttons.DrawButton;

/**
 * Created by ThomasHiron on 08/06/2015.
 */
public class AccompliceDrawing extends FrameLayout implements View.OnClickListener
{
    private Wasabi mWasabi;
    private ImageView mValidate;
    private DrawedView mDrawedView;

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
    }

    /**
     * Enregistre le dessin
     *
     * @param points
     */
    public void saveDrawing(ArrayList<Point> points)
    {
        mWasabi.saveDrawingAccomplice(points);

        /* On affiche le bouton valider */
        if(mValidate.getVisibility() == GONE)
        {
            mValidate.setVisibility(VISIBLE);

            /* Animation */
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(300);

            mValidate.startAnimation(alphaAnimation);
        }
    }

    @Override
    public void onClick(View view)
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

            file = new File(file.getAbsolutePath(), "identikit.jpg");

            /* Compression */
            b.compress(Bitmap.CompressFormat.JPEG, 95, new FileOutputStream(file));

            /* Mise à jour de la galerie */
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            getContext().sendBroadcast(mediaScanIntent);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        /* Ajout de la nouvelle vue */
        mWasabi.removeDrawingAccompliceView();
        mWasabi.addDrawedAccompliceView();
    }
}
