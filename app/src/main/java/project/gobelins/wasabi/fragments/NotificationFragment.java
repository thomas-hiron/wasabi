package project.gobelins.wasabi.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import project.gobelins.wasabi.R;


/**
 * Fragment des notifications
 */
public class NotificationFragment extends Fragment
{
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

        TextView textView = (TextView) view.findViewById(R.id.type);
        textView.setText(getArguments().getInt("type", -1) + "");

        return view;
    }
}
