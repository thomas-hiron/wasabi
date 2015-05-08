package project.gobelins.wasabi.fresco.listeners;

import android.view.View;
import android.widget.Toast;

/**
 * Joue le son
 *
 * Created by ThomasHiron on 09/05/2015.
 */
public class PlaySoundListener implements View.OnClickListener
{
    @Override
    public void onClick(View view)
    {
        Toast.makeText(view.getContext(), "Clic !", Toast.LENGTH_SHORT).show();
    }
}
