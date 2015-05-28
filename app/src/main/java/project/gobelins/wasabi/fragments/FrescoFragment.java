package project.gobelins.wasabi.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.entities.Drawing;
import project.gobelins.wasabi.entities.Entity;
import project.gobelins.wasabi.entities.Image;
import project.gobelins.wasabi.entities.Sound;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.drawing.ColorPoint;
import project.gobelins.wasabi.fresco.drawing.DrawedView;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.fresco.listeners.BeginRecordListener;
import project.gobelins.wasabi.fresco.recording.RecordView;
import project.gobelins.wasabi.fresco.views.ImageButton;
import project.gobelins.wasabi.fresco.views.SoundButton;
import project.gobelins.wasabi.interfaces.Listeners;
import project.gobelins.wasabi.utils.DateFormater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrescoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrescoFragment extends Fragment
{
    private boolean mIsLastFragment;
    private Date mDate;
    private DrawedView mDrawedView;
    private DrawedView mAccompliceView;
    private Button mStartRecordingButton;
    private ArrayList<Drawing> mDrawings;
    private ArrayList<Image> mImages;
    private ArrayList<Sound> mSounds;

    public FrescoFragment()
    {
        // Required empty public constructor
        mDrawings = new ArrayList<>();
        mImages = new ArrayList<>();
        mSounds = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DayFragment.
     */
    public static FrescoFragment newInstance(Date date)
    {
        return newInstance(date, false);
    }

    public static FrescoFragment newInstance(Date date, boolean isLastFragment)
    {
        return newInstance(new ArrayList<Entity>(), date, isLastFragment);
    }

    public static FrescoFragment newInstance(ArrayList<Entity> entities, Date date, boolean isLastFragment)
    {
        FrescoFragment frescoFragment = new FrescoFragment();
        frescoFragment.isLastFragment(isLastFragment);
        frescoFragment.setDate(date);

        /* Ajout de toutes les entités */
        for(Entity entity : entities)
        {
            if(entity instanceof Drawing)
                frescoFragment.addDrawing((Drawing) entity);
            else if(entity instanceof Image)
                frescoFragment.addImage((Image) entity);
            else if(entity instanceof Sound)
                frescoFragment.addSound((Sound) entity);
        }

        return frescoFragment;
    }

    /**
     * @param drawing Le dessin à ajouter
     */
    public void addDrawing(Drawing drawing)
    {
        mDrawings.add(drawing);
    }

    /**
     * Ajoute une liste de points au fragment (pour ne pas les perdre lorsque la vue est recyclée
     *
     * @param points Les points
     */
    public void addDrawing(ArrayList<Point> points)
    {
        Drawing drawing = new Drawing();
        drawing.setColor(((ColorPoint) points.get(0)).getColor());
        drawing.setPoints(points);

        addDrawing(drawing);
    }

    /**
     * @param image L'image à ajouter
     */
    public void addImage(Image image)
    {
        mImages.add(image);
    }

    /**
     * @param sound Le son à ajouter
     */
    public void addSound(Sound sound)
    {
        mSounds.add(sound);
    }

    private void setDate(Date date)
    {
        mDate = date;
    }

    public void setDate(String date)
    {
        mDate = DateFormater.getDateFromString(date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /* Inflate the layout for this fragment */
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_fresco, container, false);

        /* La fresque */
        Fresco fresco = (Fresco) container.getParent();

        /* La vue du dessin */
        mDrawedView = (DrawedView) view.findViewById(R.id.drawed_view);
        /* La vue du complice */
        mAccompliceView = (DrawedView) view.findViewById(R.id.accomplice_view);

        /* La date */
        TextView date = (TextView) view.findViewById(R.id.date_fragment);

        /* Changement de la date */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.FRANCE);
        day = day.substring(0, 1).toUpperCase() + day.substring(1);

        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.FRANCE);

        String date_formated = day + " " + calendar.get(Calendar.DATE) + " " + month;
        date.setText(date_formated);

        /* Si dernier fragment, on ajoute recordAudio */
        if(mIsLastFragment)
        {
            /* Inflate the RecordView */
            RecordView recordView = (RecordView) inflater.inflate(
                    R.layout.record_view, (ViewGroup) view.findViewById(R.id.fresco_fragment), false);

            /* Add the RecordView */
            view.addView(recordView);

            /* Récupération du bouton enregistrer */
            mStartRecordingButton = (Button) recordView.findViewById(R.id.start_recording);

            /* On vire la date */
            view.removeView(date);

            /* On met le listener de dessin */
            fresco.resetViews();

            /* On récupère les dessins et images */

        }

        /* On dessine toutes les courbes */
        if(mDrawings != null)
        {
            for(Drawing drawing : mDrawings)
            {
                if(drawing.getFromAccomplice())
                    mAccompliceView.draw(drawing.getPoints());
                else
                    mDrawedView.draw(drawing.getPoints());
            }
        }

        /* On ajoute toutes les images */
        if(mImages != null)
        {
            FrameLayout picturesView = (FrameLayout) view.findViewById(R.id.pictures_view);
            for(Image image : mImages)
                fresco.addNewPicture(picturesView, image.getFileName(), image.getId(), image.getPoint(), false, mIsLastFragment);
        }

        /* On ajoute tous les sons */
        if(mSounds != null)
        {
            FrameLayout soundsView = (FrameLayout) view.findViewById(R.id.sounds_view);
            for(Sound sound : mSounds)
                fresco.addNewSound(soundsView, sound.getFileName(), sound.getId(), sound.getPoint(), false, mIsLastFragment);
        }

        return view;
    }

    /**
     * @param isLastFragment Si c'est le dernier fragment, on ajoute la vue recordAudio
     */
    private void isLastFragment(boolean isLastFragment)
    {
        mIsLastFragment = isLastFragment;
    }

    /**
     * Ajoute le listener sur le bouton enregistrer
     */
    public void addRecordingListener()
    {
        mStartRecordingButton.setOnTouchListener(new BeginRecordListener());
    }

    /**
     * Supprime le listener sur le bouton enregistrer
     */
    public void removeRecordingListener()
    {
        mStartRecordingButton.setOnTouchListener(null);
    }

    /**
     * Supprime tous les listeners drag/click sur les boutons de son
     */
    public void removeDragSoundsListeners()
    {
        removeDragListeners(R.id.sounds_view);
    }

    /**
     * Supprime tous les listeners drag/click sur les boutons des images
     */
    public void removeDragImagesListeners()
    {
        removeDragListeners(R.id.pictures_view);
    }

    /**
     * Supprime tous les listeners de la resource
     *
     * @param resourceId La vue container
     */
    public void removeDragListeners(int resourceId)
    {
        View fragmentView = getView();

        if(fragmentView != null)
        {
            FrameLayout viewContainer = (FrameLayout) fragmentView.findViewById(resourceId);

            for(int i = 0, l = viewContainer.getChildCount(); i < l; ++i)
            {
                Listeners button = (Listeners) viewContainer.getChildAt(i);
                button.removeListeners();
            }
        }
    }

    /**
     * Rajoute les listeners drag/click sur les boutons de son
     */
    public void addDragSoundsListeners()
    {
        addDragListeners(R.id.sounds_view);
    }

    /**
     * Rajoute les listeners drag/click sur les boutons des images
     */
    public void addDragImagesListeners()
    {
        addDragListeners(R.id.pictures_view);
    }

    /**
     * Rajoute les listeners drag/click sur les boutons
     *
     * @param resourceId L'id de la resource
     */
    public void addDragListeners(int resourceId)
    {
        View fragmentView = getView();

        if(fragmentView != null)
        {
            FrameLayout viewContainer = (FrameLayout) fragmentView.findViewById(resourceId);

            for(int i = 0, l = viewContainer.getChildCount(); i < l; ++i)
            {
                Listeners button = (Listeners) viewContainer.getChildAt(i);
                button.addListeners();
            }
        }
    }

    /**
     * Supprime le dernier dessin
     */
    public void cancelLastDraw()
    {
        mDrawings.remove(mDrawings.size() - 1);
    }

    /**
     * @param imageButton L'image à ajouter
     */
    public void addImage(ImageButton imageButton)
    {
        Image image = new Image();
        image.setFileName(imageButton.getFileName());
        image.setPoint(imageButton.getPoint());
        image.setId(image.getId());

        addImage(image);
    }

    /**
     * Met à jour les coordonnées d'une image
     *
     * @param imageButton L'image
     * @param point
     */
    public void updateImage(ImageButton imageButton, Point point)
    {
        for(Image image : mImages)
        {
            if(image.getId() == imageButton.getDbId())
                image.setPoint(point);
        }
    }

    /**
     * @param imageButton L'image à supprimer
     */
    public void removeImage(ImageButton imageButton)
    {
        for(Image image : mImages)
        {
            if(image.getId() == imageButton.getDbId())
                mImages.remove(mImages.indexOf(image));
        }
    }

    /**
     * @param soundButton Le son à enregistrer
     */
    public void addSound(SoundButton soundButton)
    {
        Sound sound = new Sound();
        sound.setFileName(soundButton.getFileName());
        sound.setId(sound.getId());

        addSound(sound);
    }

    /**
     * @param soundButton Le son à mettre à jour
     * @param point Les nouveaux coordonnées
     */
    public void updateSound(SoundButton soundButton, Point point)
    {
        for(Sound sound : mSounds)
        {
            if(sound.getId() == soundButton.getDbId())
                sound.setPoint(point);
        }
    }

    /**
     * @param soundButton Le son à supprimer
     */
    public void removeSound(SoundButton soundButton)
    {
        for(Sound sound : mSounds)
        {
            if(sound.getId() == soundButton.getDbId())
                mSounds.remove(mSounds.indexOf(sound));
        }
    }
}