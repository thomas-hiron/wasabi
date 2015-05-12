package project.gobelins.wasabi.fresco.listeners;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.views.ImageButton;

/**
 * Classe perso qui gère le drag
 * <p/>
 * Created by ThomasHiron on 08/05/2015.
 */
public class ButtonDragListener implements View.OnTouchListener
{
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mInterfaceHidden;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ButtonDragListener(Context context)
    {
        /* On récupère la hauteur de l'écran */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        /* On replace l'élément */
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
        {
            /* Si interface non cachée (bool action ACTION_DOWN non appelé) */
            if(!mInterfaceHidden)
            {
                View root = view.getRootView();
                Fresco fresco = (Fresco) root.findViewById(R.id.fresco_container);

                fresco.hideInterfaceButtons();
                mInterfaceHidden = true;

                /* On verrouille le viewPager */
                fresco.lock();

                /* Affichage de la corbeille */
                fresco.showDustbin();
            }

            /* Les nouvelles coordonnées */
            float x = motionEvent.getRawX() - view.getWidth() / 2;
            float y = motionEvent.getRawY() - view.getHeight() / 2;

            /* Tests sur le nouveau x */
            if(x < 0)
                view.setX(0);
            else if(x > mScreenWidth - view.getWidth())
                view.setX(mScreenWidth - view.getWidth());
            else
                view.setX(x);

            /* Tests sur le nouveau y */
            if(y < 0)
                view.setY(0);
            else if(y > mScreenHeight - view.getHeight())
                view.setY(mScreenHeight - view.getHeight());
            else
                view.setY(y);

            /* Pour ne pas laisser de traces lors du drag */
            ((View) view.getParent()).invalidate();
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            /* Suppression du listener */
            view.setOnTouchListener(null);

            /* On raffiche l'interface */
            View root = view.getRootView();
            Fresco fresco = (Fresco) root.findViewById(R.id.fresco_container);

            fresco.showInterfaceButtons();
            mInterfaceHidden = false;

            /* On déverouille le viewPager */
            fresco.unlock();

            /* On cache la poubelle */
            fresco.hideDustbin();

            /* Enregistrement de la position de l'élement */
            if(view instanceof ImageButton)
                fresco.updateImage((ImageButton) view);
        }

        return true;
    }
}
