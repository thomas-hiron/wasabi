package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.views.RoundGradientChallengeView;

/**
 * Created by ThomasHiron on 24/05/2015.
 */
public class ChallengeView extends MyLayout
{
    private RoundGradientChallengeView mCounter;
    private View mCloseNotification;

    public ChallengeView(Context context)
    {
        super(context);

        inflate(context, R.layout.challenge_view, this);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* On désactive le bouton retour */
        mCloseNotification = getRootView().findViewById(R.id.close_notification);
        mCloseNotification.setVisibility(INVISIBLE);

        /* Le surnom */
        SharedPreferences prefs = getContext().getSharedPreferences(Wasabi.class.getSimpleName(), Context.MODE_PRIVATE);
        String surname = prefs.getString(Wasabi.SURNAME, "M. Patate");

        /* Ajout du surnom dans le tv */
        TextView textView = (TextView) findViewById(R.id.guess_identity_text);
        textView.setText(textView.getText() + " "  + surname + " ?");
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    @Override
    public void initialize()
    {
        /* On démarre le compteur */
        if(mCounter == null)
            mCounter = (RoundGradientChallengeView) findViewById(R.id.rounded_gradient);

        mCounter.start();
    }

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    @Override
    public void stop()
    {
        mCounter.stop();
    }
}
