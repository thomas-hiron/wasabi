package project.gobelins.wasabi.entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import project.gobelins.wasabi.fresco.drawing.ColorPoint;
import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.notifications.NotificationsManager;

/**
 * Classe représentative de la table des notifications
 * <p/>
 * Created by ThomasHiron on 11/05/2015.
 */
public class Drawing
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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public ArrayList<Point> getPoints()
    {
        return points;
    }

    public void setPoints(ArrayList<Point> points)
    {
        this.points = points;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public void setPoints(String string)
    {
        /* Extraction des points */
        String[] coords = string.split(",");
        ArrayList<Point> points = new ArrayList<Point>(coords.length / 2);

        /* Instanciation et ajout */
        for(int i = 0, l = coords.length; i < l; i += 2)
        {
            ColorPoint colorPoint = new ColorPoint();
            colorPoint.set(Float.valueOf(coords[i]), Float.valueOf(coords[i + 1]));
            colorPoint.setColor(getColor());
            points.add(colorPoint);
        }

        /* Set */
        setPoints(points);
    }
}
