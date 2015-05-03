package project.gobelins.wasabi.fresco.listeners;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ThomasHiron on 03/05/2015.
 */
public class BeginRecordListener implements View.OnTouchListener
{
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            Log.v("test", "Start recording");
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            Log.v("test", "Stop recording");
        }

        return false;
    }
}