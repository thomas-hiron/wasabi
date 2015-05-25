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
import android.util.Log;
import android.view.View;

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
    private final int TOTAL_LENGTH = 10000; /* 10s */
    private long mStartTime;
    private long mCurrentTime;

    private Handler mHandler;
    private Runnable mRunnable;

    public RoundGradientGpsView(Context context)
    {
        super(context);
    }

    public RoundGradientGpsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRed = Color.parseColor("#f26667");
        mGreen = Color.parseColor("#baeff2");

        /* On rotate la vue pour que le dégradé commence au bon endroit */
        setRotation(-90);

        mHandler = new Handler();
        mRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                mCurrentTime = System.currentTimeMillis() - mStartTime;

                /* On redessine la vue */
                invalidate();

                /* On relance */
                if(mCurrentTime < TOTAL_LENGTH)
                    mHandler.postDelayed(this, 300);
            }
        };
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

        /* Démarrage temporaire */
        start();
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
        /* Début de l'animation */
        mStartTime = System.currentTimeMillis();

        /* Démarrage du runnable */
        mHandler.post(mRunnable);
    }
}
