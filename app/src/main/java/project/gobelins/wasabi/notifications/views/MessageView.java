package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

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
    private FrameLayout mMessageContainer;

    private SoundMeter mSoundMeter;
    private Handler mHandler;
    private Runnable mRunnable;

    /* L'amplitude  */
    private final int MIN_AMPLITUDE = 20000;
    private final int APPEAR_DURATION = 10000;
    private final int DISAPPEAR_DURATION = 2000;

    /* L'intervalle de rechargement */
    private final int INTERVAL = 300;

    private Integer mCurrentWidth;
    private ValueAnimator mAnimator;
    private int mMaxWidth;
    private boolean mDown;

    public MessageView(Context context)
    {
        super(context);

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
                @Override
                public void run()
                {
                    double amplitude = mSoundMeter.getAmplitude();

                    /* On affiche le texte */
                    if(amplitude != 0)
                    {
                        /* On agrandit s'il chante */
                        if(amplitude >= MIN_AMPLITUDE)
                            animate(mMaxWidth);
                        /* Sinon on diminue */
                        else
                            animate(0);
                    }

                    mHandler.postDelayed(this, INTERVAL);
                }
            };
        }

        /* Largeur max */
        mMaxWidth = Wasabi.SCREEN_WIDTH * 2;

        /* Début de la détection du son */
        mSoundMeter = new SoundMeter();
        mSoundMeter.start();

        /* Lancement du runnable */
        mHandler.postDelayed(mRunnable, 0);

        /* Initialisation de la taille */
        mCurrentWidth = 0;
        mDown = false;

        /* Le conteneur animé */
        mMessageContainer = (FrameLayout) findViewById(R.id.message_container);
    }

    /**
     * @param to La valeur de destination
     */
    private void animate(int to)
    {
        if(!mDown)
        {
            /* On annule */
            if(mAnimator != null)
                mAnimator.cancel();

            /* Nouveau animator */
            mAnimator = slideAnimator(mCurrentWidth, to);

            /* Calcul de la durée */
            int duration = to == 0 ? DISAPPEAR_DURATION : Math.abs(mMaxWidth - mCurrentWidth * mMaxWidth / APPEAR_DURATION);

            /* Changement des paramètres et lancement */
            mAnimator.setDuration(duration);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();
        }

        /* Pour ne faire l'anim descendante qu'une seule fois */
        mDown = to == 0;
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
                mCurrentWidth = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mMessageContainer.getLayoutParams();
                layoutParams.width = mCurrentWidth;
                layoutParams.height = mCurrentWidth;
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