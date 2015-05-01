package project.gobelins.wasabi.fresco.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class FrescoActionButton extends ImageView
{
    private int mResource;
    private int mActiveResource;

    public FrescoActionButton(Context context)
    {
        super(context);
    }

    public FrescoActionButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * @return L'id de la resource
     */
    public int getResource()
    {
        return mResource;
    }

    /**
     * @param resourceId La resource par défaut
     */
    public void setResource(int resourceId)
    {
        mResource = resourceId;
    }

    /**
     * @return L'id de la resource
     */
    public int getActiveResource()
    {
        return mActiveResource;
    }

    public void setActiveResource(int activeResourceId)
    {
        mActiveResource = activeResourceId;
    }
}
