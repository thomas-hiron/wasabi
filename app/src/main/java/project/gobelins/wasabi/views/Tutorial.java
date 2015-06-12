package project.gobelins.wasabi.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import project.gobelins.wasabi.R;
import project.gobelins.wasabi.Wasabi;

/**
 * Created by ThomasHiron on 12/06/2015.
 */
public class Tutorial extends FrameLayout implements View.OnClickListener
{
    private LinearLayout mSecondPage;
    private LinearLayout mThirdPage;
    private LinearLayout mFourthPage;
    private LinearLayout mFirstPage;
    private Wasabi mWasabi;

    public Tutorial(Context context)
    {
        super(context);
    }

    public Tutorial(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        mFirstPage = (LinearLayout) findViewById(R.id.tutorial_page_1);
        mSecondPage = (LinearLayout) findViewById(R.id.tutorial_page_2);
        mThirdPage = (LinearLayout) findViewById(R.id.tutorial_page_3);
        mFourthPage = (LinearLayout) findViewById(R.id.tutorial_page_4);

        /* Affiche la première page */
        showFirstPage();
    }

    public void setWasabi(Wasabi wasabi)
    {
        mWasabi = wasabi;
    }

    @Override
    public void onClick(View view)
    {
        /* Suppression du listener */
        view.setOnClickListener(null);

        /* Go to second page */
        if(view.getId() == R.id.tutorial_next_1)
        {
            hideFirstPage();
            showSecondPage();
        }
        /* Go to third page */
        else if(view.getId() == R.id.tutorial_next_2)
        {
            hideSecondPage();
            showThirdPage();
        }
        /* Go to third page */
        else if(view.getId() == R.id.tutorial_next_3)
        {
            hideThirdPage();
            showFourthPage();
        }
        /* Go to first page from second */
        else if(view.getId() == R.id.tutorial_prev_2)
        {
            hideSecondPage();
            showFirstPage();
        }
        /* Go to second page from third */
        else if(view.getId() == R.id.tutorial_prev_3)
        {
            hideThirdPage();
            showSecondPage();
        }
        /* Go to third page from fourth */
        else if(view.getId() == R.id.tutorial_prev_4)
        {
            hideFourthPage();
            showThirdPage();
        }
        /* On affiche la home */
        else if(view.getId() == R.id.tutorial_next_4)
        {
            mWasabi.removeTutorialView();
            mWasabi.addHome();
        }
    }

    /**
     * Affiche la première page
     */
    private void showFirstPage()
    {
        mFirstPage.setVisibility(VISIBLE);

        /* Clic sur le bouton suivant de la première page */
        ImageView next = (ImageView) findViewById(R.id.tutorial_next_1);

        next.setOnClickListener(this);
    }

    /**
     * Cache la première page
     */
    private void hideFirstPage()
    {
        mFirstPage.setVisibility(GONE);
    }

    /**
     * Affiche la deuxième page
     */
    private void showSecondPage()
    {
        mSecondPage.setVisibility(VISIBLE);

        /* Listener sur suivant et précédent */
        ImageView prev = (ImageView) findViewById(R.id.tutorial_prev_2);
        ImageView next = (ImageView) findViewById(R.id.tutorial_next_2);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    /**
     * Cache la deuxième page
     */
    private void hideSecondPage()
    {
        mSecondPage.setVisibility(GONE);
    }

    /**
     * Affiche la troisième page
     */
    private void showThirdPage()
    {
        mThirdPage.setVisibility(VISIBLE);

        /* Listener sur suivant et précédent */
        ImageView prev = (ImageView) findViewById(R.id.tutorial_prev_3);
        ImageView next = (ImageView) findViewById(R.id.tutorial_next_3);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    /**
     * Cache la troisième page
     */
    private void hideThirdPage()
    {
        mThirdPage.setVisibility(GONE);
    }

    /**
     * Affiche la quatrième page
     */
    private void showFourthPage()
    {
        mFourthPage.setVisibility(VISIBLE);

        /* Listener sur suivant et précédent */
        ImageView prev = (ImageView) findViewById(R.id.tutorial_prev_4);
        ImageView next = (ImageView) findViewById(R.id.tutorial_next_4);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    /**
     * Cache la quatrième page
     */
    private void hideFourthPage()
    {
        mFourthPage.setVisibility(GONE);
    }
}
