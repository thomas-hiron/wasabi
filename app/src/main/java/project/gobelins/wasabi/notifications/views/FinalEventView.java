package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;

/**
 * Created by ThomasHiron on 24/05/2015.
 */
public class FinalEventView extends MyLayout implements View.OnClickListener
{
    private ImageView mAcceptButton;
    private ImageView mDeclineButton;
    private TextView mAcceptText;
    private TextView mDeclineText;
    private TextView m7DaysText;
    private TextView mMeetingText;
    private View mCloseNotification;
    private boolean mFinalEventAccepted;

    public FinalEventView(Context context)
    {
        super(context);

        mDisplayCloseNotification = false;
        inflate(context, R.layout.final_event_view, this);
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

        /* Evenement déjà accepté */
        mFinalEventAccepted = prefs.getBoolean(Wasabi.FINAL_EVENT_ACCEPTED, false);

        /* Initialisation des variables */
        mAcceptButton = (ImageView) findViewById(R.id.accept_final_event);
        mAcceptText = (TextView) findViewById(R.id.accept_final_event_text);
        mDeclineButton = (ImageView) findViewById(R.id.decline_final_event);
        mDeclineText = (TextView) findViewById(R.id.decline_final_event_text);
        m7DaysText = (TextView) findViewById(R.id.in_7_days);
        mMeetingText = (TextView) findViewById(R.id.accomplice_meeting);

        /* Evenement final déjà accepté, on cache les boutons */
        if(mFinalEventAccepted)
        {
            mAcceptButton.setVisibility(GONE);
            mAcceptText.setVisibility(GONE);
            mDeclineButton.setVisibility(GONE);
            mDeclineText.setVisibility(GONE);
            m7DaysText.setVisibility(GONE);
            mMeetingText.setVisibility(VISIBLE);

            mDisplayCloseNotification = true;
            mCloseNotification.setVisibility(VISIBLE);
        }
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    @Override
    public void initialize()
    {
        /* Ajout listener accepter */
        if(!mFinalEventAccepted)
            mAcceptButton.setOnClickListener(this);
    }

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    @Override
    public void stop()
    {
        /* Suppression du listener sur le bouton accepter */
        if(!mFinalEventAccepted)
            mAcceptButton.setOnClickListener(null);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.accept_final_event)
            acceptButtonClicked();
    }

    /**
     * Bouton accepter le challenge cliqué
     */
    private void acceptButtonClicked()
    {
        /* Clic sur le bouton accepter, on anime les boutons */
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);

        /* Opacité sur les textes */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);

        mAcceptButton.startAnimation(scaleAnimation);
        mDeclineButton.startAnimation(scaleAnimation);

        mAcceptText.startAnimation(alphaAnimation);
        mDeclineText.startAnimation(alphaAnimation);
        m7DaysText.startAnimation(alphaAnimation);

        /* Affichage des nouveaux éléments */
        scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(300);
        scaleAnimation.setInterpolator(new OvershootInterpolator());

        alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);
        alphaAnimation.setStartOffset(700);

        mCloseNotification.setVisibility(VISIBLE);
        mMeetingText.setVisibility(VISIBLE);

        mCloseNotification.startAnimation(scaleAnimation);
        mMeetingText.startAnimation(alphaAnimation);

        /* Ajout dans les sharedPrefs */
        SharedPreferences prefs = getContext().getSharedPreferences(Wasabi.class.getSimpleName(), Context.MODE_PRIVATE);
        prefs.edit().putBoolean(Wasabi.FINAL_EVENT_ACCEPTED, true).apply();
    }
}
