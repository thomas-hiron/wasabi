package project.gobelins.wasabi.fresco;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * La poubelle
 *
 * Created by ThomasHiron on 12/05/2015.
 */
public class Dustbin extends ImageView
{
    private int mWidth;
    private int mHeight;
    private int mX;
    private int mY;

    public Dustbin(Context context)
    {
        super(context);
    }

    public Dustbin(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
    }

    public JSONObject getCoordinates() throws JSONException
    {
        /* Initialisation si null */
        if(mWidth == 0)
        {
            mWidth = getWidth();
            mHeight = getHeight();
            mX = (int) getX();
            mY = (int) getY();
        }

        /* Construction d'un JSON */
        JSONObject coordinates = new JSONObject();
        coordinates.put("width", mWidth);
        coordinates.put("height", mHeight);
        coordinates.put("x", mX);
        coordinates.put("y", mY);

        return coordinates;
    }
}
