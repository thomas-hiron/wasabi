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
import android.widget.Toast;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;

/**
 * Created by ThomasHiron on 08/06/2015.
 */
public class WelcomeView extends FrameLayout implements View.OnClickListener
{
    private Wasabi mWasabi;

    public WelcomeView(Context context)
    {
        super(context);
    }

    public WelcomeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* Clic sur le bouton suivant */
        ImageView nextStep = (ImageView) findViewById(R.id.next_step);

        nextStep.setOnClickListener(this);

        /* On scale le logo correctement */
        ImageView view = (ImageView) findViewById(R.id.welcome_logo);
        Drawable drawing = view.getBackground();

        /* Les dimensions du parent */
        View parent = (View) view.getParent();

        /* Eviter les bugs */
        if(drawing == null)
            return;

        Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

        /* Récupération des dimensions et de la boîte voulue */
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(400 * Wasabi.SCREEN_WIDTH / 1080);

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
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private int dpToPx(int dp)
    {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Override
    public void onClick(View view)
    {
        view.setOnClickListener(null);

        /* Suppression de la vue et ajoue de la suivante */
        mWasabi.removeWelcomeView();
        mWasabi.addDrawingAccomplice();
    }

    public void setWasabi(Wasabi wasabi)
    {
        mWasabi = wasabi;
    }
}
