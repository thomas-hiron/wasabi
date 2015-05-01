package project.gobelins.wasabi.fresco;

import android.graphics.Color;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class Colors
{
    private final String WHITE = "#FFFFFF";
    private final String BLACK = "#1A1A1A";
    private final String GREEN = "#4ed6c2";
    private final String YELLOW = "#ffd646";
    private final String RED = "#f26667";
    private final String PINK = "#c190bd";

    private int[] mColors;

    /**
     * Construit les couleurs
     */
    public Colors()
    {
        mColors = new int[6];

        mColors[0] = Color.parseColor(WHITE);
        mColors[1] = Color.parseColor(BLACK);
        mColors[2] = Color.parseColor(GREEN);
        mColors[3] = Color.parseColor(YELLOW);
        mColors[4] = Color.parseColor(RED);
        mColors[5] = Color.parseColor(PINK);
    }

    /**
     * @return Toutes les couleurs
     */
    public int[] getColors()
    {
        return mColors;
    }
}
