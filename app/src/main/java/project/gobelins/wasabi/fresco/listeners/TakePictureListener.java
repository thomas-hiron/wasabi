package project.gobelins.wasabi.fresco.listeners;

import android.view.View;
import android.widget.Toast;

import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.views.FrescoActionButton;

/**
 * Created by ThomasHiron on 01/05/2015.
 */
public class TakePictureListener implements View.OnClickListener
{
    private Fresco mFresco;

    public TakePictureListener(Fresco fresco, FrescoActionButton mPictureButton)
    {
        mFresco = fresco;
    }

    @Override
    public void onClick(View view)
    {
        Toast.makeText(view.getContext(), "On prend une photo", Toast.LENGTH_SHORT).show();
    }
}
