package project.gobelins.wasabi.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.fresco.listeners.BeginRecordListener;
import project.gobelins.wasabi.fresco.recording.RecordView;
import project.gobelins.wasabi.fresco.views.SoundButton;
import project.gobelins.wasabi.interfaces.Listeners;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrescoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrescoFragment extends Fragment
{
    private Button mStartRecordingButton;
    private boolean mIsLastFragment;

    public FrescoFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DayFragment.
     */
    public static FrescoFragment newInstance()
    {
        return newInstance(false);
    }

    public static FrescoFragment newInstance(boolean isLastFragment)
    {
        FrescoFragment frescoFragment = new FrescoFragment();
        frescoFragment.isLastFragment(isLastFragment);

        return frescoFragment;
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