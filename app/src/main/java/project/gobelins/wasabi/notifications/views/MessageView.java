package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import project.gobelins.wasabi.R;

/**
 * Affiche un message à l'écran, si caché, prend aléatoirement entre chant et toucher
 * Created by ThomasHiron on 28/04/2015.
 */
public class MessageView extends LinearLayout
{
    private String mMessage;

    public MessageView(Context context)
    {
        super(context);

        /* Le message temporairement stocké en dur */
        mMessage = "Le Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant " +
                "impression. Le Lorem Ipsum est le faux texte standard de l'imprimerie depuis les années 1500, " +
                "quand un peintre anonyme assembla ensemble des morceaux de texte pour réaliser un livre.";

        /* On inflate la vue */
        inflate(context, R.layout.message_view, this);

        /* Changement du texte */
        TextView message = (TextView) findViewById(R.id.message);
        message.setText(mMessage);
    }
}