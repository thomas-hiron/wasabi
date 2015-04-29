package project.gobelins.wasabi.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.entities.Notification;
import project.gobelins.wasabi.notifications.NotificationsTypes;
import project.gobelins.wasabi.notifications.views.MessageView;
import project.gobelins.wasabi.notifications.views.MyLayout;


/**
 * Fragment des notifications
 */
public class NotificationFragment extends Fragment
{
    private boolean mHidePrevious = false;
    private boolean mHideNext = false;
    private MyLayout mCustomView;

    public NotificationFragment()
    {
        // Required empty public constructor
    }

    /**
     * Nouvelle instance du fragment
     *
     * @param notification La notification
     * @return Un nouveau fragment
     */
    public static NotificationFragment newInstance(Notification notification)
    {
        NotificationFragment myFragment = new NotificationFragment();

        Bundle args = new Bundle();
        args.putParcelable("notification", notification);
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

        /* Récupération de la notification */
        Notification notification = getArguments().getParcelable("notification");

        /* En fonction de la notification */
        switch(notification.getType())
        {
            /* Les messages (à faire apparaître ou non) */
            case NotificationsTypes.MESSAGES:

                /* Création de la vue */
                mCustomView = new MessageView(getActivity());
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.notification_container);

                /* Ajout du message à la vue */
                layout.addView(mCustomView);

                break;

            default:

                /* Changement du texte */
                TextView textView = (TextView) view.findViewById(R.id.type);
                textView.setText(notification.getId() + " " + notification.isRead() + " " + notification.getType() + " " + notification.getDate().toString());

                break;
        }

        /* On cache le précédent */
        if(mHidePrevious)
            view.findViewById(R.id.previous).setVisibility(View.INVISIBLE);
        /* On cache le suivant */
        if(mHideNext)
            view.findViewById(R.id.next).setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {

        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void setHideNext(boolean hideNext)
    {
        this.mHideNext = hideNext;
    }

    public void setHidePrevious(boolean hidePrevious)
    {
        this.mHidePrevious = hidePrevious;
    }

    /**
     * @return La vue personnalisée : vidéo, son, message,...
     */
    public MyLayout getCustomView()
    {
        return mCustomView;
    }
}
