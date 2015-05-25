package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
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
        mPaint.setStrokeWidth(30);

        mRed = Color.parseColor("#f26667");
        mGreen = Color.parseColor("#8dcccf");

        /* On rotate la vue pour que le dégradé commence au bon endroit */
        setRotation(-90);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;

        /* Position du cercle */
        mPosition = new RectF(30, 30, mWidth - 30, mWidth - 30);

        /* Le dégradé */
        mGradient = new SweepGradient(mWidth / 2, mWidth / 2, mGreen, mRed);

        /* Dessin */
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(mWidth != 0)
        {
            mPaint.setShader(mGradient);
            RectF rectF = mPosition;
            canvas.drawArc(rectF, -90, -360, false, mPaint);
        }
    }
}
