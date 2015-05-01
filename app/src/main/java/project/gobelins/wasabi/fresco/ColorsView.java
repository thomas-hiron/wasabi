package project.gobelins.wasabi.fresco;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import project.gobelins.wasabi.R;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class ColorsView extends LinearLayout
{
    public ColorsView(Context context)
    {
        super(context);
    }

    public ColorsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        /* Récupértation des couleurs */
        Colors colorsObject = new Colors();
        int[] colors = colorsObject.getColors();

        /* L'inflater */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /* Création des boutons et ajout des listeners */
        for(int color : colors)
        {
            /* Inflation du bouton */
            Button b = (Button) inflater.inflate(R.layout.color_button, this, false);

            /* Changement du background */
            Drawable background = b.getBackground();
            background.setColorFilter(new LightingColorFilter(color, color));

            /* Ajout de la vue */
            addView(b);
        }
    }
}
