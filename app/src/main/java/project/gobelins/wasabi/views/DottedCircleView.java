package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ThomasHiron on 25/05/2015.
 */
public class DottedCircleView extends View
{
    private DashPathEffect mDashPath;
    private Paint mPaint;
    private int mWidth;

    public DottedCircleView(Context context)
    {
        super(context);
    }

    public DottedCircleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#8dcccf"));

        /* Pour la bordure en pointillés */
        mDashPath = new DashPathEffect(new float[]{7, 7}, (float) 1.0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;

        /* Dessin */
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(mWidth != 0)
        {
            mPaint.setPathEffect(mDashPath);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(6);
            canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - 40, mPaint);
        }
    }
}
