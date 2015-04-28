package project.gobelins.wasabi.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.interfaces.OnNextNotificationListener;
import project.gobelins.wasabi.interfaces.OnPreviousNotificationListener;


/**
 * Fragment des notifications
 */
public class NotificationFragment extends Fragment
{
    private OnNextNotificationListener mListenerNext;
    private OnPreviousNotificationListener mListenerPrevious;

    public NotificationFragment()
    {
        // Required empty public constructor
    }

    /**
     * Nouvelle instance du fragment
     *
     * @param type Le type de la notification
     * @return Un nouveau fragment
     */
    public static NotificationFragment newInstance(int type)
    {
        NotificationFragment myFragment = new NotificationFragment();

        Bundle args = new Bundle();
        args.putInt("type", type);
        myFragment.setArguments(args);

        return myFragment;
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        /* Changement du texte */
        TextView textView = (TextView) view.findViewById(R.id.type);
        textView.setText(getArguments().getInt("type", -1) + "");

        /* Ajout du listener sur le bouton next */
        Button next = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListenerNext.onNextNotificationListener();
            }
        });

        /* Ajout du listener sur le bouton précédent */
        Button prev = (Button) view.findViewById(R.id.previous);
        prev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListenerPrevious.onPreviousNotificationListener();
            }
        });

        return view;
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListenerNext = (OnNextNotificationListener) activity;
            mListenerPrevious = (OnPreviousNotificationListener) activity;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
