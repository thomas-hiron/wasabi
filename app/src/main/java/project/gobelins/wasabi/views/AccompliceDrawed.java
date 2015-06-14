package project.gobelins.wasabi.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;

/**
 * La vue du complice dessiné, permet de renseiger le surnom
 * <p/>
 * Created by ThomasHiron on 08/06/2015.
 */
public class AccompliceDrawed extends LinearLayout implements View.OnClickListener
{
    private Wasabi mWasabi;

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

        /* Listener sur le bouton valider */
        ButtonQuicksand button = (ButtonQuicksand) findViewById(R.id.validate_surname);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        /* Suppression du listener */
        view.setOnClickListener(null);

        /* Validation du surnom si non vide */
        EditTextQuicksand editText = (EditTextQuicksand) findViewById(R.id.surname);

        /* Le texte */
        String text = editText.getText().toString().trim();

        /* Si champs valide */
        if(text.length() == 0)
        {
            Toast.makeText(getContext(), "Le champ ne doit pas être vide", Toast.LENGTH_SHORT).show();
            view.setOnClickListener(this);
        }
        else
        {
            /* Ajout du surnom dans les sharedPref */
            SharedPreferences prefs = getContext().getSharedPreferences(Wasabi.class.getSimpleName(), Context.MODE_PRIVATE);
            prefs.edit().putString(Wasabi.SURNAME, text).apply();

            /* Affichage du tutorial */
            mWasabi.removeDrawedAccompliceView();
            mWasabi.addTutorialView();
        }
    }

    public void setWasabi(Wasabi wasabi)
    {
        mWasabi = wasabi;
    }
}