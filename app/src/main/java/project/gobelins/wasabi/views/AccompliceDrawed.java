package project.gobelins.wasabi.views;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;

import project.gobelins.wasabi.R;

/**
 * La vue du complice dessiné, permet de renseiger le surnom
 * <p/>
 * Created by ThomasHiron on 08/06/2015.
 */
public class AccompliceDrawed extends LinearLayout
{
    public AccompliceDrawed(Context context)
    {
        super(context);
    }

    public AccompliceDrawed(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        /* Ajout du dessin du complice */
        ImageView imageView = (ImageView) findViewById(R.id.drawed_accomplice);

        /* Le fichier */
        String storageString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                "/wasabi/identikit.png";
        File file = new File(storageString);

        /* Ajout dans l'imageView */
        Picasso.with(getContext()).load(file).into(imageView);
    }
}