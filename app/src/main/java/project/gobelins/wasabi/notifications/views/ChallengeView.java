package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.listeners.AlphaRemoveListener;
import project.gobelins.wasabi.views.RoundGradientChallengeView;

/**
 * Created by ThomasHiron on 24/05/2015.
 */
public class ChallengeView extends MyLayout implements View.OnClickListener
{
    private RoundGradientChallengeView mCounter;
    private View mCloseNotification;
    private LinearLayout mFormView;
    private Button mValidateFullName;
    private LinearLayout mStartChallenge;
    private Button mStartChallengeButton;
    private LinearLayout mChallenge;

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

        SharedPreferences prefs = getContext().getSharedPreferences(Wasabi.class.getSimpleName(), Context.MODE_PRIVATE);
        String surname = prefs.getString(Wasabi.SURNAME, "M. Patate");

        /* Ajout du surnom dans le tv */
        TextView textView = (TextView) findViewById(R.id.guess_identity_text);
        textView.setText(textView.getText() + " " + surname + " ?");

        mFormView = (LinearLayout) findViewById(R.id.challenge_form);
        mFormView.setVisibility(VISIBLE);

        mValidateFullName = (Button) mFormView.findViewById(R.id.validate_full_name);
        mValidateFullName.setOnClickListener(this);
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
    }

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    @Override
    public void stop()
    {
        mCounter.stop();
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.validate_full_name)
            validateFullNameClicked();
        else if(view.getId() == R.id.ready)
            startChallengeClicked();
        else if(view.getId() == R.id.counter)
            counterClicked();

        view.setOnClickListener(null);
    }

    /**
     * Bouton valider le formulaire cliqué
     */
    private void validateFullNameClicked()
    {
        EditText fullName = (EditText) findViewById(R.id.full_name);
        String text = fullName.getText().toString().trim();

        if(text.length() != 0)
        {
            Toast.makeText(getContext(), "Le champ ne doit pas être vide", Toast.LENGTH_SHORT).show();
            mValidateFullName.setOnClickListener(this);
        }
        else
        {
            /* Formulaire caché via animation */
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(500);

            mFormView.startAnimation(alphaAnimation);
            alphaAnimation.setAnimationListener(new AlphaRemoveListener((FrameLayout) mFormView.getParent(), mFormView));

            /* Affichage de la nouvelle vue */
            mStartChallenge = (LinearLayout) findViewById(R.id.start_challenge);
            mStartChallenge.setVisibility(View.VISIBLE);

            alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(500);

            mStartChallenge.startAnimation(alphaAnimation);

            /* Listener sur le bouton prêt */
            mStartChallengeButton = (Button) mStartChallenge.findViewById(R.id.ready);
            mStartChallengeButton.setOnClickListener(this);
        }
    }

    /**
     * Démarrage du challenge
     */
    private void startChallengeClicked()
    {
         /* Vue caché via animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(500);

        mStartChallenge.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new AlphaRemoveListener((FrameLayout) mStartChallenge.getParent(), mStartChallenge));

            /* Affichage de la nouvelle vue */
        mChallenge = (LinearLayout) findViewById(R.id.challenge);
        mChallenge.setVisibility(View.VISIBLE);

        alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);

        mChallenge.startAnimation(alphaAnimation);

        /* Démarrage du décompte */
        mCounter.start();

        /* Listener sur le décompte */
        LinearLayout counter = (LinearLayout) mChallenge.findViewById(R.id.counter);
        counter.setOnClickListener(this);
    }

    /**
     * Clic sur le compteur on simule l'arrivée
     */
    private void counterClicked()
    {
         /* Décompte caché via animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(500);

        mChallenge.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new AlphaRemoveListener((FrameLayout) mChallenge.getParent(), mChallenge));

        /* Affichage de la nouvelle vue */
        LinearLayout experienceOver = (LinearLayout) findViewById(R.id.experience_over);
        experienceOver.setVisibility(View.VISIBLE);

        alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);

        experienceOver.startAnimation(alphaAnimation);
        mCloseNotification.startAnimation(alphaAnimation);
    }
}
