package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.listeners.ChangeDrawingColorListener;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class ColorButton extends FrameLayout
{
    private Button mButton;
    private View mActiveState;
    private int mColor;
    private boolean mActive;

    public ColorButton(Context context)
    {
        super(context);
    }

    public ColorButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mActive = false;
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        mButton = (Button) findViewById(R.id.color_button);
        mActiveState = findViewById(R.id.color_active_stroke);

        /* Changement du background */
        Drawable background = mButton.getBackground();
        background.setColorFilter(new LightingColorFilter(mColor, mColor));

        /* Ajout du listener */
        addListener();

        /* Si bouton activé */
        if(mActive)
            activate();
    }

    public int getColor()
    {
        return mColor;
    }

    public void setColor(int color)
    {
        mColor = color;
    }

    /**
     * Active le bouton (scale et affichage du contour)
     */
    public void activate()
    {
        mButton.setScaleX(0.6f);
        mButton.setScaleY(0.6f);

        mActiveState.setVisibility(VISIBLE);
    }

    /**
     * Désactive le bouton
     */
    public void disable()
    {
        mButton.setScaleX(1);
        mButton.setScaleY(1);

        mActiveState.setVisibility(GONE);
    }

    /**
     * Ajoute le listener
     */
    public void addListener()
    {
        mButton.setOnClickListener(new ChangeDrawingColorListener());
    }

    /**
     * Pour activer le bouton par défaut
     */
    public void setActive()
    {
        mActive = true;
    }
}
