package project.gobelins.wasabi.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Date;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.entities.Drawing;
import project.gobelins.wasabi.entities.Entity;
import project.gobelins.wasabi.entities.Image;
import project.gobelins.wasabi.fresco.Fresco;
import project.gobelins.wasabi.fresco.drawing.DrawedView;
import project.gobelins.wasabi.fresco.listeners.BeginRecordListener;
import project.gobelins.wasabi.fresco.recording.RecordView;
import project.gobelins.wasabi.fresco.views.FrescoViewPager;
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
    private Button mStartRecordingButton;
    private ArrayList<Drawing> mDrawings;
    private ArrayList<Image> mImages;

    public FrescoFragment()
    {
        // Required empty public constructor
        mDrawings = new ArrayList<>();
        mImages = new ArrayList<>();
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
     * @param image L'image à ajouter
     */
    public void addImage(Image image)
    {
        mImages.add(image);
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
        }

        /* On dessine toutes les courbes */
        if(mDrawings != null)
        {
            for(Drawing drawing : mDrawings)
                mDrawedView.draw(drawing.getPoints());
        }

        /* On ajoute toutes les images */
        if(mImages != null)
        {
            FrameLayout picturesView = (FrameLayout) view.findViewById(R.id.pictures_view);
            for(Image image : mImages)
                fresco.addNewPicture(picturesView, image.getFileName(), image.getId(), image.getPoint(), false, mIsLastFragment);
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
}