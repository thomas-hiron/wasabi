package project.gobelins.wasabi.fresco;

import android.graphics.Color;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class Colors
{
    private final String BLACK = "#1A1A1A";
    private final String RED1 = "#f26667";
    private final String RED2 = "#f69494";

    private int[] mColors;

    /**
     * Construit les couleurs
     */
    public Colors()
    {
        mColors = new int[3];

        mColors[0] = Color.parseColor(BLACK);
        mColors[1] = Color.parseColor(RED1);
        mColors[2] = Color.parseColor(RED2);
    }

    /**
     * @return Toutes les couleurs
     */
    public int[] getColors()
    {
        return mColors;
    }
}