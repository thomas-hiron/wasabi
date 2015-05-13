package project.gobelins.wasabi.fresco.listeners;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.views.ImageButton;
import project.gobelins.wasabi.interfaces.DraggableElement;

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

    private int mDustWidth;
    private int mDustHeight;
    private int mDustX;
    private int mDustY;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ButtonDragListener(Context context, JSONObject dustbinCoordinates)
    {
        /* On récupère la hauteur de l'écran */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;

        try
        {
            /* Dimensions */
            mDustWidth = dustbinCoordinates.getInt("width");
            mDustHeight = dustbinCoordinates.getInt("height");

            /* Position */
            mDustX = dustbinCoordinates.getInt("x");
            mDustY = dustbinCoordinates.getInt("y");

            /* On Supprime un tier de la poubelle */
            int excess = mDustWidth / 4;

            mDustWidth -= excess*2;
            mDustHeight -= excess*2;
            mDustX += excess;
            mDustY += excess;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        /* On replace l'élément */
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
        {
            DraggableElement viewButton = (DraggableElement) view;

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

            /* On test si on est sur la poubelle */
            int eventX = (int) motionEvent.getRawX();
            int eventY = (int) motionEvent.getRawY();

            if(eventX >= mDustX
                    && eventX <= mDustX + mDustWidth
                    && eventY >= mDustY
                    && eventY <= mDustY + mDustHeight)
                viewButton.scaleToDelete(eventX, eventY);
            else
                viewButton.scaleToNormal(eventX, eventY);

            /* Les nouvelles coordonnées */
            float x = eventX - viewButton.getCustomWidth() / (viewButton.isDeleting() ? 1 : 2);
            float y = eventY - viewButton.getCustomHeight() / (viewButton.isDeleting() ? 1 : 2);

            /* Tests sur le nouveau x */
            if(x < 0)
                view.setX(0);
            else if(x > mScreenWidth - viewButton.getCustomWidth())
                view.setX(mScreenWidth - viewButton.getCustomWidth());
            else
                view.setX(x);

            /* Tests sur le nouveau y */
            if(y < 0)
                view.setY(0);
            else if(y > mScreenHeight - viewButton.getCustomHeight())
                view.setY(mScreenHeight - viewButton.getCustomHeight());
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
            {
                boolean delete = ((ImageButton) view).testDelete();

                /* Suppression */
                if(delete)
                    ((ImageButton) view).delete(motionEvent.getRawX(), motionEvent.getRawY());
                /* Mise à jour */
                else
                    fresco.updateImage((ImageButton) view);
            }
        }

        return true;
    }
}