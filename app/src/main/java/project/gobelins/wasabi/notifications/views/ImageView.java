package project.gobelins.wasabi.notifications.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.httpRequests.AsyncPostImageRequest;
import project.gobelins.wasabi.httpRequests.AsyncPostRequests;

/**
 * Affiche un message à l'écran, si caché, prend aléatoirement entre chant et toucher
 * Created by ThomasHiron on 28/04/2015.
 */
public class ImageView extends MyLayout implements View.OnClickListener
{
    private Notification mNotification;
    private Wasabi mWasabi;
    private android.widget.ImageView mTakePicture;
    private android.widget.ImageView mAccomplice;

    public ImageView(Context context)
    {
        super(context);
    }

    public ImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ImageView(Wasabi wasabi, Notification notification)
    {
        super(wasabi.getApplicationContext());

        /* Récupération de l'image via une requête */
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("request_id", String.valueOf(notification.getId())));

        /* Exécution de la requête */
        new AsyncPostImageRequest(nameValuePairs, this).execute(
                Wasabi.URL + "/api/" + Wasabi.getApiKey() + "/request/customImage"
        );

        mWasabi = wasabi;
        mNotification = notification;
    }

    /**
     * Initialise la vue (lance le recorder pour le message,...)
     */
    @Override
    public void initialize()
    {
        if(mTakePicture == null)
            mTakePicture = (android.widget.ImageView) findViewById(R.id.take_picture);

        if(mTakePicture != null)
            mTakePicture.setOnClickListener(this);

        /* Chargement du complice dessiné */
        if(mAccomplice == null)
        {
            mAccomplice = (android.widget.ImageView) findViewById(R.id.drawed_accomplice);

            /* Ajout de l'image */
            String storageString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                    "/wasabi/identikit.png";
            File file = new File(storageString);
            Picasso.with(getContext()).load(file).into(mAccomplice);

            /* Le surnom */
            SharedPreferences prefs = getContext().getSharedPreferences(Wasabi.class.getSimpleName(), Context.MODE_PRIVATE);
            String surname = prefs.getString(Wasabi.SURNAME, "M. Patate");

            /* Changement du texte */
            TextView textView = (TextView) findViewById(R.id.text_view_surname);
            textView.setText(textView.getText() + " " + surname);
        }
    }

    /**
     * Stop (Met en pause la vidéo, le son,...)
     */
    @Override
    public void stop()
    {
        mTakePicture.setOnClickListener(null);
    }

    /**
     * Réception de l'image OK
     *
     * @param imageUrl L'url relative de l'image
     */
    public void imageSuccess(String imageUrl)
    {
        inflate(getContext(), R.layout.image_view, this);

        /* Récupération de l'image */
        android.widget.ImageView image = (android.widget.ImageView) findViewById(R.id.custom_image);

        /* Ajout de l'image dans la vue */
        Picasso.with(getContext()).load(Wasabi.URL + "/" + imageUrl).into(image);

        mTakePicture.setOnClickListener(this);
    }

    /**
     * Une erreur s'est produite
     */
    public void imageError()
    {
        inflate(getContext(), R.layout.error_view, this);
    }

    @Override
    public void onClick(View view)
    {
        mWasabi.onTakePictureCustomImage();
    }

    /**
     * Photo OK
     *
     * @param photoPath
     */
    public void photoOk(String photoPath)
    {
        mTakePicture.setOnClickListener(null);

        /* Encodage du fichier en base64 */
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String picture64 = Base64.encodeToString(b, Base64.DEFAULT);

        /* Appel à l'API */
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("picture64", picture64));
        nameValuePairs.add(new BasicNameValuePair("request_id", String.valueOf(mNotification.getId())));

        /* Exécution de la requête */
        new AsyncPostRequests(nameValuePairs).execute(
                Wasabi.URL + "/api/" + Wasabi.getApiKey() + "/request/saveCustomImage"
        );

        /* On change les vues */
        LinearLayout customView = (LinearLayout) findViewById(R.id.custom_image_view);
        LinearLayout indiceView = (LinearLayout) findViewById(R.id.custom_image_indice);

        customView.setVisibility(GONE);
        indiceView.setVisibility(VISIBLE);
    }
}