package project.gobelins.wasabi.fresco.listeners;

import android.view.View;
import android.widget.Toast;

import project.gobelins.wasabi.fresco.views.SoundButton;

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
        SoundButton soundButton = (SoundButton) view;
        Toast.makeText(view.getContext(), soundButton.getFileName(), Toast.LENGTH_SHORT).show();
    }
}
