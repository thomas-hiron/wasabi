package project.gobelins.wasabi.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Gère les dates
 * <p/>
 * Created by ThomasHiron on 12/05/2015.
 */
public class DateFormater
{
    private static Date mToday = new Date();
    public final static String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * @return La date du jour formatée
     */
    public static String getTodayAsString()
    {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        return dateFormat.format(mToday);
    }

    /**
     * @param pDate La date sous forme de chaîne
     * @return Un objet Date
     */
    public static Date getDateFromString(String pDate)
    {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.FRANCE);
        Date date = null;
        try
        {
            date = format.parse(pDate);
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * @return La date du jour sans les heures
     */
    public static Date getStrictToday()
    {
        return getDateFromString(getTodayAsString());
    }

    /**
     * @return La date du jour avec les heures
     */
    public static Date getToday()
    {
        return mToday;
    }
}
