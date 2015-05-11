package project.gobelins.wasabi.entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.notifications.NotificationsManager;

/**
 * Classe représentative de la table des notifications
 *
 * Created by ThomasHiron on 11/05/2015.
 */
public class Drawings
{
    private int id;
    private Date date;
    private ArrayList<Point> points;
    private int color;

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setDate(String pDate)
    {
        DateFormat format = new SimpleDateFormat(NotificationsManager.DATE_FORMAT, Locale.FRANCE);
        Date date;
        try
        {
            date = format.parse(pDate);
            this.date = date;
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
    }
}
