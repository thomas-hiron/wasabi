package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import project.gobelins.wasabi.R;

/**
 * Created by ThomasHiron on 25/05/2015.
 */
public class RoundGradientGpsView extends View
{
    private int mGreen;
    private int mRed;
    private Paint mPaint;
    private int mWidth;
    private Shader mGradient;
    private RectF mPosition;

    /* Paramétrage */
    private final int TOTAL_LENGTH = 63000;
    private long mStartTime;
    private long mCurrentTime;

    private Handler mHandler;
    private Runnable mRunnable;
    private TextViewQuicksand mMinutes;
    private TextViewQuicksand mSeconds;
    private FrameLayout mTimerContainer;
    private boolean mStart;

    public RoundGradientGpsView(Context context)
    {
        super(context);
    }

    public RoundGradientGpsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mStart = false;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRed = Color.parseColor("#f26667");
        mGreen = Color.parseColor("#baeff2");

        /* On rotate la vue pour que le dégradé commence au bon endroit */
        setRotation(-90);

        mRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                mCurrentTime = System.currentTimeMillis() - mStartTime;

                /* On décrémente le chrono */
                changeTimer();

                /* On redessine la vue */
                invalidate();

                /* On relance */
                if(mCurrentTime < TOTAL_LENGTH && mStart)
                    mHandler.postDelayed(this, 200);
            }
        };
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        mTimerContainer = (FrameLayout) getParent();

        /* Récupération des champs */
        mMinutes = (TextViewQuicksand) mTimerContainer.findViewById(R.id.challenge_minutes);
        mSeconds = (TextViewQuicksand) mTimerContainer.findViewById(R.id.challenge_seconds);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;

        /* Position du cercle */
        mPosition = new RectF(30, 30, mWidth - 30, mWidth - 30);

        /* Le dégradé */
        mGradient = new SweepGradient(
                mWidth / 2, mWidth / 2,
                new int[]{mRed, mGreen, mGreen, mRed, mRed},
                new float[]{0, 0.05f, 0.4f, 0.6f, 1}
        );

        mPaint.setShader(mGradient);

        /* Dessin */
        invalidate();
    }

    /**
     * Change le timer
     */
    private void changeTimer()
    {
        /* Temps restant en secondes */
        int time_left = (int) ((TOTAL_LENGTH - mCurrentTime) / 1000);

        /* Pour éviter les valeurs négatives */
        if(time_left < 0)
            time_left = 0;

        /* Nombre de minutes restantes */
        int minutes_left = (int) Math.floor(time_left / 60);

        /* On enlève les minutes */
        time_left -= minutes_left * 60;

        /* Nombre de secondes restantes */
        int seconds_left = time_left;

        /* Changement des textes */
        if(mMinutes != null)
        {
            mMinutes.setText(String.valueOf(minutes_left));
            mSeconds.setText(String.valueOf(seconds_left));
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.drawArc(mPosition, 0, -360 * mCurrentTime / TOTAL_LENGTH, false, mPaint);
    }

    /**
     * Démarre le compteur
     */
    public void start()
    {
        if(!mStart)
        {
            /* Animation démarrée */
            mStart = true;

            /* Animation du timer */
            mTimerContainer.setVisibility(VISIBLE);

            /* Premier lancement, on lance l'animation et initialise le compteur */
            if(mStartTime == 0)
            {
                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(300);
                scaleAnimation.setInterpolator(new OvershootInterpolator());

                mTimerContainer.startAnimation(scaleAnimation);

                /* Début de l'animation */
                mStartTime = System.currentTimeMillis();

                /* Instanciation ici (dans le thread principal) */
                mHandler = new Handler();
            }

            /* Démarrage du runnable */
            mHandler.post(mRunnable);
        }
    }

    /**
     * Stoppe le compteur
     */
    public void stop()
    {
        mStart = false;
    }
}
