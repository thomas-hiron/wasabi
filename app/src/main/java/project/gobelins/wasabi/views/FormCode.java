package project.gobelins.wasabi.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.httpRequests.AsyncPostCodeRequest;

/**
 * La vue contenant le formulaire de renseignement du code
 * <p/>
 * Created by ThomasHiron on 21/05/2015.
 */
public class FormCode extends FrameLayout implements View.OnClickListener
{
    private View mValidate;
    private EditTextQuicksand mCode;
    private Wasabi mWasabi;

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

        /* Le mCode */
        mCode = (EditTextQuicksand) findViewById(R.id.code);

        /* Ajout du listener sur le bouton */
        mValidate = findViewById(R.id.validate_code);
        mValidate.setOnClickListener(this);
    }

    private int dpToPx(int dp)
    {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * Clic sur le bouton valider
     */
    @Override
    public void onClick(View view)
    {
        /* Suppression du listener */
        mValidate.setOnClickListener(null);

        /* Changement de la couleur pour le retour */
        mValidate.setBackgroundColor(Color.parseColor("#F58585"));

        boolean isValid = checkValidCode();

        /* Envoi de la requête */
        if(isValid)
        {
            /* Construction des données POST */
            List<NameValuePair> nameValuePairs = new ArrayList<>(1);
            nameValuePairs.add(new BasicNameValuePair("code", mCode.getText().toString().toUpperCase()));

            new AsyncPostCodeRequest(nameValuePairs, this)
                    .execute(Wasabi.URL + "/api/code");
        }
    }

    /**
     * Vérifie la longueur du code
     */
    private boolean checkValidCode()
    {
        String code = mCode.getText().toString();

        /* On remet l'alpha, le listener et on affiche un toast */
        if(code.length() < 4)
            error();

        return code.length() >= 4;
    }

    /**
     * Réinitialise l'alpha et le listener
     */
    public void error()
    {
        mValidate.setBackgroundColor(Color.parseColor("#f26667"));
        Toast.makeText(getContext(), "Le code n'est pas valide, veuillez réessayer", Toast.LENGTH_SHORT).show();

        mValidate.setOnClickListener(this);
    }

    /**
     * Liaison OK, on cache la vue
     */
    public void success()
    {
        mWasabi.formCodeSuccess();
    }

    public void setActivity(Wasabi activity)
    {
        this.mWasabi = activity;
    }
}
