package project.gobelins.wasabi;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.apache.http.NameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.fresco.DrawingsManager;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.drawing.DrawView;
import project.gobelins.wasabi.fresco.drawing.DrawedView;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.fresco.listeners.DrawingListener;
import project.gobelins.wasabi.homeAnimation.views.AnimationLayout;
import project.gobelins.wasabi.httpRequests.AsyncPostDrawingsRequest;
import project.gobelins.wasabi.interfaces.OnFrescoClosed;
import project.gobelins.wasabi.interfaces.OnFrescoOpened;
import project.gobelins.wasabi.interfaces.OnNotificationClosed;
import project.gobelins.wasabi.interfaces.OnNotificationOpened;
import project.gobelins.wasabi.interfaces.OnPictureListener;
import project.gobelins.wasabi.listeners.CircleAnimationListener;
import project.gobelins.wasabi.notifications.AsyncNotificationInflater;
import project.gobelins.wasabi.notifications.NotificationsManager;
import project.gobelins.wasabi.notifications.NotificationsTypes;
import project.gobelins.wasabi.notifications.views.GPSView;
import project.gobelins.wasabi.notifications.views.MyLayout;
import project.gobelins.wasabi.utils.DateFormater;
import project.gobelins.wasabi.views.FormCode;

public class Wasabi extends FragmentActivity implements OnFrescoOpened, OnFrescoClosed, OnNotificationOpened,
        OnNotificationClosed, OnPictureListener
{
    public final static String TAG = "Wasabi";
    public final static String API_KEY = "api_key";
    public final static String REQUEST_ID = "request_id";
    public final static String REQUEST_TYPE = "request_type";
    public final static String REQUEST_PHASE = "request_phase";
    public final static String ACCOMPLICE_DRAWED = "accomplice_drawed";
    public final static String CUSTOM_ANIM_NOT_PLAYED = "custom_anim_played";

    private final int REQUEST_IMAGE = 1;
    private final int REQUEST_GPS = 2;
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
    private static int mPhaseNumber;
    private FormCode mFormCode;
    private boolean mAnimPlayed;
    private boolean mDisplayCustomView;
    private ImageView mUnexpectedText;
    private ImageView mNotificationCloseButton;
    private FrameLayout mDrawAccompliceView;

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
            mAnimLayout.setNotification(mLastNotification);

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

        /* Récupération de la phase */
        getPhaseNumberFromPrefs();

        /* L'élément racine de la vue de l'application */
        mAppContainer = (FrameLayout) findViewById(R.id.app_container);

        /* Animation jamais jouée */
        mAnimPlayed = false;
        mDisplayCustomView = false;

        /* Instanciation du manager des notifications */
        mNotificationsManager = new NotificationsManager(getContentResolver());

        /* Récupération de la dernière notification */
        mLastNotification = mNotificationsManager.getLast();

        /* ---- TEMPORAIRE ---- */
        mAnimPlayed = true;
        homeAnimationEnd();
        /* ---- FIN TEMPORAIRE ---- */

        /* Dès que la taille de la vue principale change, on remet le mode immersif (fermeture du clavier) */
        mAppContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                immersiveMode(true);
            }
        });

        /* On récupère les dessins du complice s'il y en a */
        checkAccompliceDrawings();
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
        if(mCustomView != null)
            mCustomView.initialize();

        /* On supprime le listener du bouton sur la fresque */
        mFrescoButton.setOnClickListener(null);

        /* On marque la notif comme lu */
        mNotificationsManager.markRead(mLastNotification);
    }

    /**
     * La notification a été fermée
     */
    @Override
    public void onNotificationClosed()
    {
        /* On remet les listeners */
        mNotificationButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerNotification));
        mFrescoButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerFresco));

        /* On cache la vue */
        mRevealContainerNotification.setVisibility(View.INVISIBLE);

        int id = mLastNotification.getType();

        /* Selon les notifs, on stoppe le processus (vidéo, son,...) */
        switch(id)
        {
            /* Les messages (à faire apparaître ou non) */
            case NotificationsTypes.MESSAGES:

                break;

            default:

                break;
        }

        mCustomView.stop();
    }

    /**
     * On a cliqué sur le bouton prendre une photo
     */
    @Override
    public void onTakePicture()
    {
        takePicture(REQUEST_IMAGE);
    }

    /**
     * On a cliqué sur le bouton prendre une photo du GPS
     */
    public void onTakePictureGPS()
    {
        takePicture(REQUEST_GPS);
    }

    /**
     * On prendre une photo
     *
     * @param request L'id de la requête
     */
    public void takePicture(int request)
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
                startActivityForResult(takePictureIntent, request);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /* Photo bien prise */
        if(resultCode == RESULT_OK && (requestCode == REQUEST_IMAGE || requestCode == REQUEST_GPS))
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

            /* On met à jour la galerie */
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);

            /* Traitements spécifiques */
            if(requestCode == REQUEST_IMAGE)
            {
                /* On l'ajoute à la fresque */
                if(mFresco != null)
                    mFresco.addNewPicture(mCurrentPhotoPath);
            }
            else
            {
                /* On change la vue */
                ((GPSView) mCustomView).photoOk(mCurrentPhotoPath);
            }
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
        /* Pour passer dans defaut si aucune notif */
        Notification notification = mLastNotification != null ? mLastNotification : new Notification();

        switch(notification.getType())
        {
            /* Affichage du challenge */
            case NotificationsTypes.CHALLENGES:

                SharedPreferences prefs = getSharedPreferences(Wasabi.class.getSimpleName(), MODE_PRIVATE);
                boolean customAnimNotPlayed = prefs.getBoolean(Wasabi.CUSTOM_ANIM_NOT_PLAYED, false);

                /* Pour forcer l'affichage de la customView */
                mDisplayCustomView = customAnimNotPlayed;

                /* Suppression du marqueur */
                prefs.edit().remove(Wasabi.CUSTOM_ANIM_NOT_PLAYED).apply();

                /* On ajoute la home sans l'animer */
                addHome(!customAnimNotPlayed);

                if(customAnimNotPlayed)
                {
                    mAppContainer.setBackgroundColor(Color.WHITE);

                    /* On cache la home */
                    mUnexpectedText.setVisibility(View.INVISIBLE);
                    mFrescoButton.setVisibility(View.INVISIBLE);
                    mUnexpectedText.setVisibility(View.INVISIBLE);

                    /* Suppression des listeners */
                    mFrescoButton.setOnClickListener(null);
                    if(mNotificationButton != null)
                    {
                        mNotificationButton.setVisibility(View.INVISIBLE);
                        mNotificationButton.setOnClickListener(null);
                    }
                }
                else
                    mAppContainer.setBackgroundResource(R.drawable.home);

                break;

            default:

                /* Changement du background */
                mAppContainer.setBackgroundResource(R.drawable.home);

                /* On affiche le formulaire */
                if(mApiKey == null)
                    addFormCode();
                else
                    addHome();

                break;
        }

        /* Suppression de la vidéo */
        mAppContainer.removeView(mAnimLayout);
        mAnimLayout = null;
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
     * Liaison du code OK
     */
    public void formCodeSuccess()
    {
        /* Récupération de la clé */
        getApiKeyFromPrefs();

        /* Suppression de la vue */
        removeFormCode();

        /* Ajout de la vue pour dessiner */
        addDrawingAccomplice();

        /* On envoie le registration_id si première connexion */
        RegistrationIdManager registrationIdManager = new RegistrationIdManager(this);
        registrationIdManager.getRegistrationID();

        /* Récupération des dessins de l'utilisateur */
        getAccompliceDrawings();
    }

    /**
     * Supprime le formulaire
     */
    private void removeFormCode()
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
     * Ajoute la home avec animation par défaut
     */
    private void addHome()
    {
        addHome(true);
    }

    /**
     * Ajoute la home
     *
     * @param animate Si on doit animer
     */
    private void addHome(boolean animate)
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

        /* La notif si != null (inflate, ajout de la vue et du listener) */
        if(mLastNotification != null)
        {
            mRevealContainerNotification = getLayoutInflater().inflate(R.layout.notification, mAppContainer, false);
            mAppContainer.addView(mRevealContainerNotification);
            mNotificationButton.setOnClickListener(new CircleAnimationListener(this, mRevealContainerNotification));

            /* Affichage du bouton */
            mNotificationButton.setVisibility(View.VISIBLE);

            /* On ajoute la vue en background */
            new AsyncNotificationInflater(this, mLastNotification).execute();
        }

        /* Le texte */
        mUnexpectedText = (ImageView) findViewById(R.id.unexpected_text);

        /* Animation des éléments */
        mFrescoButton.setVisibility(View.VISIBLE);
        mUnexpectedText.setVisibility(View.VISIBLE);

        if(animate)
        {
            /* Animation */
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(1500);

            mUnexpectedText.startAnimation(alphaAnimation);
            mFrescoButton.startAnimation(alphaAnimation);
            if(mLastNotification != null)
                mNotificationButton.startAnimation(alphaAnimation);
        }

        /* Si portrait robot actif, on supprime la vue */
        if(mDrawAccompliceView != null)
        {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(1500);

            mDrawAccompliceView.startAnimation(alphaAnimation);

            /* Suppression de la vue lorsque l'animation est terminée */
            alphaAnimation.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {

                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    mAppContainer.removeView(mDrawAccompliceView);
                    mDrawAccompliceView = null;
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {

                }
            });
        }
    }

    /**
     * Ajoute la vue permettant de dessiner le complice
     */
    private void addDrawingAccomplice()
    {
        /* Inflation de la vue */
        mDrawAccompliceView = (FrameLayout)
                getLayoutInflater().inflate(R.layout.drawing_accomplice_view, mAppContainer, false);

        mAppContainer.addView(mDrawAccompliceView);

        /* Animation */
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1500);

        mDrawAccompliceView.startAnimation(alphaAnimation);

        /* Les vues */
        DrawView drawView = (DrawView) mDrawAccompliceView.findViewById(R.id.draw_view);
        DrawedView drawedView = (DrawedView) mDrawAccompliceView.findViewById(R.id.drawed_view);

        drawView.setOnTouchListener(new DrawingListener(drawView, drawedView, this));

        /* Bouton valider */
        ImageView validate = (ImageView) mDrawAccompliceView.findViewById(R.id.identikit_completed);

        /* Listener sur le bouton terminer */
        validate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addHome();
            }
        });
    }

    /**
     * Enregistre le portrait robot
     *
     * @param points
     */
    public void saveDrawing(ArrayList<Point> points)
    {
        DrawingsManager drawingsManager = new DrawingsManager(getContentResolver());
        drawingsManager.saveDrawingAccomplice(points);
    }

    /**
     * Récupére la clé api
     */
    private void getApiKeyFromPrefs()
    {
        if(mApiKey == null)
        {
            SharedPreferences prefs = getSharedPreferences(Wasabi.class.getSimpleName(), Context.MODE_PRIVATE);

            mApiKey = prefs.getString(API_KEY, null);
        }
    }

    /**
     * Récupére le numéro de la phase
     */
    private void getPhaseNumberFromPrefs()
    {
        if(mPhaseNumber == 0)
        {
            SharedPreferences prefs = getSharedPreferences(Wasabi.class.getSimpleName(), Context.MODE_PRIVATE);

            mPhaseNumber = prefs.getInt(REQUEST_PHASE, 1);
        }
    }

    /**
     * Récupére le numéro de la phase
     */
    public static int getPhaseNumber()
    {
        return mPhaseNumber;
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

    /**
     * Ajoute la vue perso
     *
     * @param customView
     */
    public void setCustomView(MyLayout customView)
    {
        mCustomView = customView;
        mNotificationCloseButton = (ImageView) findViewById(R.id.close_notification);

        /* Récupération du premier enfant */
        FrameLayout child = (FrameLayout) mRevealContainerNotification.findViewById(R.id.notification_container);

        /* Ajout du message à la vue */
        child.removeView(mCustomView);
        child.addView(mCustomView, 0);

        /* On l'affiche si forcé */
        if(mDisplayCustomView)
        {
            mRevealContainerNotification.setVisibility(View.VISIBLE);
            final AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(1500);

            mCustomView.startAnimation(alphaAnimation);
            mNotificationCloseButton.startAnimation(alphaAnimation);

            final CircleAnimationListener circleAnimationListener = new CircleAnimationListener(this, mRevealContainerNotification);

            /* Pour initialiser à la fin */
            alphaAnimation.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {

                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    alphaAnimation.setAnimationListener(null);
                    mCustomView.initialize();

                    /* On remet la home derrière */
                    mUnexpectedText.setVisibility(View.VISIBLE);
                    mFrescoButton.setVisibility(View.VISIBLE);
                    mUnexpectedText.setVisibility(View.VISIBLE);
                    if(mNotificationButton != null)
                        mNotificationButton.setVisibility(View.VISIBLE);

                    /* Changement du background de la home */
                    mAppContainer.setBackgroundResource(R.drawable.home);

                    /* Listener au clic sur le bouton fermer */
                    mNotificationCloseButton.setOnClickListener(circleAnimationListener);
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {

                }
            });
        }
    }

    /**
     * Récupère les dessins du complice
     */
    private void checkAccompliceDrawings()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(Wasabi.class.getSimpleName(), MODE_PRIVATE);

        boolean accompliceDrawed = sharedPreferences.getBoolean(ACCOMPLICE_DRAWED, false);

        /* Récupération en asyncTask */
        if(accompliceDrawed)
        {
            getAccompliceDrawings();

            /* Suppression des préférences */
            sharedPreferences.edit().remove(ACCOMPLICE_DRAWED).apply();
        }
    }

    /**
     * Récupère les nouveaux dessins
     */
    private void getAccompliceDrawings()
    {
        /* Construction des données POST */
        new AsyncPostDrawingsRequest(new ArrayList<NameValuePair>(0), this)
                .execute(Wasabi.URL + "/api/" + Wasabi.getApiKey() + "/fresco/drawings");
    }
}