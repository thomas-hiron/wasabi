package project.gobelins.wasabi;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.homeAnimation.views.AnimationLayout;
import project.gobelins.wasabi.interfaces.OnFrescoClosed;
import project.gobelins.wasabi.interfaces.OnFrescoOpened;
import project.gobelins.wasabi.interfaces.OnNotificationClosed;
import project.gobelins.wasabi.interfaces.OnNotificationOpened;
import project.gobelins.wasabi.interfaces.OnPictureListener;
import project.gobelins.wasabi.listeners.CircleAnimationListener;
import project.gobelins.wasabi.notifications.NotificationsManager;
import project.gobelins.wasabi.notifications.NotificationsTypes;
import project.gobelins.wasabi.notifications.views.MessageView;
import project.gobelins.wasabi.notifications.views.MyLayout;
import project.gobelins.wasabi.utils.DateFormater;
import project.gobelins.wasabi.views.FormCode;

public class Wasabi extends FragmentActivity implements OnFrescoOpened, OnFrescoClosed, OnNotificationOpened,
        OnNotificationClosed, OnPictureListener
{
    public final static String TAG = "Wasabi";
    public final static String API_KEY = "api_key";

    private final int REQUEST_IMAGE = 1;
    private final int IMAGE_WIDTH = 500;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static final String URL = "http://wasabi-experience.fr";

    private NotificationsManager mNotificationsManager;
    private FrameLayout mAppContainer;
    private AnimationLayout mAnimLayout;
    private View mRevealContainerFresco;
    private View mRevealContainerNotification;
    private ImageView mFrescoButton;
    private ImageView mNotificationButton;
    private MyLayout mCustomView;
    private Notification mLastNotification;
    private String mCurrentPhotoPath;
    private Fresco mFresco;
    private static String mApiKey;
    private FormCode mFormCode;
    private boolean mAnimPlayed;

    @Override
    public void onStart()
    {
        super.onStart();

        /* Pour ne jouer l'animation qu'une seule fois */
        if(!mAnimPlayed)
        {
            /* Inflation de la home */
            mAnimLayout = (AnimationLayout) getLayoutInflater().inflate(
                    R.layout.home_animation, mAppContainer, false);

            mAnimLayout.setActivity(this);

            /* Ajout de la vue */
            mAppContainer.addView(mAnimLayout);

            mAnimPlayed = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Ajout de la vue */
        setContentView(R.layout.activity_wasabi);

        /* Récupération des dimensions de l'écran */
        getScreenMetrics();

        /* Récupération de la clé API */
        getApiKeyFromPrefs();

//        /* Instanciation du manager des notifications */
//        mNotificationsManager = new NotificationsManager(getContentResolver());
//
//        /* Récupération de la dernière notification */
//        mLastNotification = mNotificationsManager.getLast();
//
//        /* On envoie le registration_id si première connexion */
//        RegistrationIdManager registrationIdManager = new RegistrationIdManager(this);
//        registrationIdManager.getRegistrationID();

        /* L'élément racine de la vue de l'application */
        mAppContainer = (FrameLayout) findViewById(R.id.app_container);

        /* Animation jamais jouée */
        mAnimPlayed = false;

//        /* La notif si != null (inflate, ajout de la vue et du listener) */
//        if(mLastNotification != null)
//        {
//            mRevealContainerNotification = getLayoutInflater().inflate(R.layout.notification, mAppContainer, false);
//            mAppContainer.addView(mRevealContainerNotification);
//            mNotificationButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerNotification));
//        }
//        /* Sinon on cache le bouton */
//        else
//            mNotificationButton.setVisibility(View.GONE);

        /* Dès que la taille de la vue principale change, on remet le mode immersif (fermeture du clavier) */
        mAppContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                immersiveMode(true);
            }
        });
    }

    /**
     * On cache la navigation bar et la notification bare
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        immersiveMode(hasFocus);
    }

    /**
     * La fresque a été ouverte
     */
    @Override
    public void onFrescoOpened()
    {

    }

    /**
     * La fresque a été fermée
     */
    @Override
    public void onFrescoClosed()
    {
        /* On remet le listener */
        mFrescoButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerFresco));

        /* On cache la vue */
        mRevealContainerFresco.setVisibility(View.INVISIBLE);
    }

    /**
     * La notification a été ouverte
     */
    @Override
    public void onNotificationOpened()
    {
        /* Récupération du premier enfant */
        FrameLayout child = (FrameLayout) ((ViewGroup) mRevealContainerNotification).getChildAt(0);

        /* En fonction de la notification */
        switch(mLastNotification.getType())
        {
            /* Les messages (à faire apparaître ou non) */
            case NotificationsTypes.MESSAGES:

                /* Création de la vue */
                mCustomView = new MessageView(this);

                /* Ajout du message à la vue */
                child.addView(mCustomView);

                mCustomView.initialize();

                break;

            default:

                break;
        }
    }

    /**
     * La notification a été fermée
     */
    @Override
    public void onNotificationClosed()
    {
        /* On remet le listener */
        mNotificationButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerNotification));

        /* On cache la vue */
        mRevealContainerNotification.setVisibility(View.INVISIBLE);

        int id = 6;

        /* Selon les notifs, on stoppe le processus (vidéo, son,...) */
        switch(id)
        {
            /* Les messages (à faire apparaître ou non) */
            case NotificationsTypes.MESSAGES:

                mCustomView.stop();

                break;

            default:

                break;
        }

        /* Récupération du premier enfant pour supprimer la customView */
        FrameLayout child = (FrameLayout) ((ViewGroup) mRevealContainerNotification).getChildAt(0);
        child.removeView(mCustomView);
    }

    /**
     * On a cliqué sur le bouton prendre une photo
     */
    @Override
    public void onTakePicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        /* Il y a bien une activité pour le résultat */
        if(takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            /* Création du fichier de sortie */
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch(IOException ex)
            {
                Log.v(TAG, "Une erreur s'est produite");
            }

            /* On continue uniquement si le fichier est bien créé */
            if(photoFile != null)
            {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /* Photo bien prise */
        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK)
        {
            /* Dimensions de la source */
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, options);

            int srcWidth = options.outWidth;
            int desiredWidth = IMAGE_WIDTH;

            /* On redimensionne uniquement si besoin */
            if(desiredWidth > srcWidth)
                desiredWidth = srcWidth;

            /* Calcul du bon rapport inSampleSize/scale, réduction de l'utilisation de la mémoire */
            int inSampleSize = 1;
            while(srcWidth / 2 > desiredWidth)
            {
                srcWidth /= 2;
                inSampleSize *= 2;
            }

            float desiredScale = (float) desiredWidth / srcWidth;

            /* Decodage avec inSampleSize */
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inSampleSize = inSampleSize;
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);

            /* Redimensionnement */
            Matrix matrix = new Matrix();
            matrix.postScale(desiredScale, desiredScale);
            Bitmap scaledBitmap = Bitmap.createBitmap(sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(), matrix, true);
            sampledSrcBitmap = null;

            /* Enregistrement */
            FileOutputStream out = null;
            try
            {
                out = new FileOutputStream(mCurrentPhotoPath);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }

            scaledBitmap = null;

            /* On l'ajoute à la fresque */
            if(mFresco != null)
                mFresco.addNewPicture(mCurrentPhotoPath);

            /* On met à jour la galerie */
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        }
    }

    /**
     * Créer un fichier temporaire pour la photo
     *
     * @return Un fichier temporaire
     * @throws IOException
     */
    private File createImageFile() throws IOException
    {
        /* Nom du fichier */
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(DateFormater.getToday());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/wasabi";

        /* On créer le fichier si besoin */
        File storageDir = new File(storageString);
        if(!storageDir.exists())
            storageDir.mkdirs();

        File image = File.createTempFile(
                imageFileName,  /* prefixe */
                ".jpg",         /* suffixe */
                storageDir      /* dossier */
        );

        /* Fichier à utiliser ensuite */
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Récupère les dimensions de l'appareil
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void getScreenMetrics()
    {
        if(SCREEN_WIDTH == 0)
        {
            /* On récupère la hauteur de l'écran */
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            SCREEN_WIDTH = displayMetrics.widthPixels;
            SCREEN_HEIGHT = displayMetrics.heightPixels;
        }
    }

    /**
     * Animation terminée, supprime l'animation de départ
     */
    public void homeAnimationEnd()
    {
        mAppContainer.removeView(mAnimLayout);
        mAnimLayout = null;

        /* Changement du background */
        mAppContainer.setBackgroundResource(R.drawable.home);

        /* On affiche le formulaire */
        if(mApiKey == null)
            addFormCode();
        else
            addHome();
    }

    /**
     * Fait apparaître le formulaire pour renseigner le code
     */
    public void addFormCode()
    {
        /* Inflation du formulaire */
        mFormCode = (FormCode) getLayoutInflater().inflate(R.layout.form_code, mAppContainer, false);

        /* Ajout de la vue */
        mAppContainer.addView(mFormCode);

        mFormCode.setActivity(this);
        mFormCode.setVisibility(View.VISIBLE);

        /* Animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1500);
        mFormCode.startAnimation(alphaAnimation);
    }

    /**
     * Supprime le formulaire
     */
    public void removeFormCode()
    {
        /* Animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(1500);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation animation)
            {
                mAppContainer.removeView(mFormCode);
            }

            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        mFormCode.startAnimation(alphaAnimation);
    }

    /**
     * Ajoute la home
     */
    public void addHome()
    {
        /* Ajout des listeners d'animation */
        mFrescoButton = (ImageView) findViewById(R.id.open_fresco);
        mNotificationButton = (ImageView) findViewById(R.id.open_notification);

        /* La fresque toujours présente (inflate, ajout de la vue et du listener) */
        mRevealContainerFresco = getLayoutInflater().inflate(R.layout.fresco, mAppContainer, false);
        mAppContainer.addView(mRevealContainerFresco);
        mFrescoButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerFresco));

        /* On initialise la fresque et le viewPager */
        mFresco = (Fresco) mRevealContainerFresco.findViewById(R.id.fresco_container);
        mFresco.initViewPager(getSupportFragmentManager());
        mFresco.setPictureListener(this);

        /* Le texte */
        ImageView text = (ImageView) findViewById(R.id.unexpected_text);

        /* Animation des éléments */
        mFrescoButton.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);

        /* Animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1500);

        text.startAnimation(alphaAnimation);
        mFrescoButton.startAnimation(alphaAnimation);
    }

    /**
     * Récupére la clé api
     */
    public void getApiKeyFromPrefs()
    {
        if(mApiKey == null)
        {
            SharedPreferences prefs = getSharedPreferences(Wasabi.class.getSimpleName(), Context.MODE_PRIVATE);

            mApiKey = prefs.getString(API_KEY, null);
        }
    }

    /**
     * @return La clé API
     */
    public static String getApiKey()
    {
        return mApiKey;
    }

    /* Rentre dans le mode immersif */
    private void immersiveMode(boolean hasFocus)
    {
        if(hasFocus && android.os.Build.VERSION.SDK_INT >= 19) // Mode immersif
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}