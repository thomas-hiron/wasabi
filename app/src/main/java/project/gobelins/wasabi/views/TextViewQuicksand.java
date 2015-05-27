package project.gobelins.wasabi.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import project.gobelins.wasabi.R;

/**
 * Created by ThomasHiron on 21/05/2015.
 */
public class TextViewQuicksand extends TextView
{
    private String mFontWeight;
    private boolean mItalic;

    public TextViewQuicksand(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewQuicksand(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextViewQuicksand);

        mFontWeight = "Regular";
        mItalic = false;

        final int N = a.getIndexCount();
        for(int i = 0; i < N; ++i)
        {
            int attr = a.getIndex(i);
            switch(attr)
            {
                case R.styleable.TextViewQuicksand_font_weight:

                    mFontWeight = a.getString(attr).toLowerCase();
                    mFontWeight = mFontWeight.substring(0,1).toUpperCase() + mFontWeight.substring(1);

                    break;

                case R.styleable.TextViewQuicksand_font_style:

                    mItalic = true;

                    break;
            }
        }
        a.recycle();

        init();
    }

    public TextViewQuicksand(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Quicksand-" + mFontWeight + ".ttf");
        setTypeface(tf, mItalic ? Typeface.ITALIC : Typeface.NORMAL);
    }
}
