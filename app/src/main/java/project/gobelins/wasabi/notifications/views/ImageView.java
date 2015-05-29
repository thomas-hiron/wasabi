package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.httpRequests.AsyncPostImageRequest;

/**
 * Affiche un message à l'écran, si caché, prend aléatoirement entre chant et toucher
 * Created by ThomasHiron on 28/04/2015.
 */
public class ImageView extends MyLayout
{
    public ImageView(Context context)
    {
        super(context);
    }

    public ImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ImageView(Context context, Notification notification)
    {
        super(context);

        /* Récupération de l'image via une requête */
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("request_id", String.valueOf(notification.getId())));

        /* Exécution de la requête */
        new AsyncPostImageRequest(nameValuePairs, this).execute(
                Wasabi.URL + "/api/" + Wasabi.getApiKey() + "/request/customImage"
        );
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    @Override
    public void initialize()
    {

    }

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    @Override
    public void stop()
    {

    }

    /**
     * Réception de l'image OK
     *
     * @param imageUrl L'url relative de l'image
     * @param legend
     */
    public void imageSuccess(String imageUrl, String legend)
    {
        inflate(getContext(), R.layout.image_view, this);

        /* Récupération de l'image */
        android.widget.ImageView image = (android.widget.ImageView) findViewById(R.id.custom_image);
        TextView legendText = (TextView) findViewById(R.id.legend);

        /* Ajout de l'image dans la vue */
        Picasso.with(getContext()).load(Wasabi.URL + "/" + imageUrl).into(image);

        /* Changement de la légende */
        legendText.setText(legend);
    }

    /**
     * Une erreur s'est produite
     */
    public void imageError()
    {
        inflate(getContext(), R.layout.error_view, this);
    }
}