package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;

/**
 * La vue contenant le formulaire de renseignement du code
 * <p/>
 * Created by ThomasHiron on 21/05/2015.
 */
public class FormCode extends FrameLayout
{
    public FormCode(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FormCode(Context context)
    {
        super(context);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* On scale le logo correctement */
        ImageView view = (ImageView) findViewById(R.id.logo_wasabi);
        Drawable drawing = view.getBackground();

        /* Les dimensions du parent */
        View parent = (View) view.getParent();
        int paddingLeft = parent.getPaddingLeft();
        int parentWidth = parent.getWidth() - paddingLeft * 2;

        /* Eviter les bugs */
        if(drawing == null)
            return;

        Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

        /* Récupération des dimensions et de la boîte voulue */
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(230 * Wasabi.SCREEN_WIDTH / 1080);

        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        /* Création d'un bitmap pour la vue */
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        view.setImageDrawable(result);

        /* Changement des dimensions de la view */
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private int dpToPx(int dp)
    {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
