package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.notifications.utils.SoundMeter;

/**
 * Affiche un message à l'écran, si caché, prend aléatoirement entre chant et toucher
 * Created by ThomasHiron on 28/04/2015.
 */
public class MessageView extends MyLayout
{
    private String mMessage;
    private String mCurrentMessage;
    private FrameLayout mMessageContainer;

    private SoundMeter mSoundMeter;
    private Handler mHandler;
    private Runnable mRunnable;

    /* L'amplitude  */
    private final int MIN_AMPLITUDE = 20000;

    /* L'intervalle de rechargement */
    private int mInterval = 300;
    private final int MAX_INTERVAL = 300;
    private final int MIN_INTERVAL = 10;
    private final int PAS_INTERVAL = 50;

    public MessageView(Context context)
    {
        super(context);

        /* Le message temporairement stocké en dur */
        mMessage = "Le Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant impression.";

        /* Le message qui s'affiche progressivement à l'écran */
        mCurrentMessage = "";

        /* On inflate la vue */
        inflate(context, R.layout.message_view, this);
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    @Override
    public void initialize()
    {
        if(mHandler == null)
        {
            /* Instanciation et initialisation du handler et du runnable */
            mHandler = new Handler();
            mRunnable = new Runnable()
            {
                private int counter = 0;

                @Override
                public void run()
                {
                    double amplitude = mSoundMeter.getAmplitude();

                    /* On affiche le texte */
                    if(amplitude >= MIN_AMPLITUDE || mInterval < MAX_INTERVAL)
                    {
                        /* On incrémente le message courant */
                        mCurrentMessage = mMessage.substring(0, counter++);

                        /* On accélère l'intervalle s'il chante */
                        if(mInterval > MIN_INTERVAL && amplitude >= MIN_AMPLITUDE)
                            mInterval -= PAS_INTERVAL;
                        /* Sinon on décélère l'intervalle */
                        else if(mInterval < MAX_INTERVAL)
                            mInterval += PAS_INTERVAL;
                    }

                    /* Message non affiché en entier, on relance */
                    if(mCurrentMessage.length() != mMessage.length())
                        mHandler.postDelayed(this, mInterval);
                    /* Sinon on stoppe l'enregistrement */
                    else
                        mSoundMeter.stop();
                }
            };
        }
        /* Début de la détection du son */
        mSoundMeter = new SoundMeter();
        mSoundMeter.start();

        /* Lancement du runnable */
        mHandler.postDelayed(mRunnable, 0);

        mMessageContainer = (FrameLayout) findViewById(R.id.message_container);

        /* On lance l'animation */
        ValueAnimator mAnimator = slideAnimator(0, Wasabi.SCREEN_WIDTH);
        mAnimator.setDuration(5000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }


    /**
     * Anime la hauteur du conteneur du dégradé
     *
     * @param start La valeur de départ
     * @param end   La hauteur finale
     * @return L'animator
     */
    private ValueAnimator slideAnimator(int start, int end)
    {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                /* On met à jour la hauteur */
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mMessageContainer.getLayoutParams();
                layoutParams.width = value;
                layoutParams.height = value;
                mMessageContainer.setLayoutParams(layoutParams);
            }
        });

        return animator;
    }

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    @Override
    public void stop()
    {
        mSoundMeter.stop();
    }
}