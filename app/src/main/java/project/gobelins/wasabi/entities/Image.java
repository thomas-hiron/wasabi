package project.gobelins.wasabi.entities;

import java.util.Date;

import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.utils.DateFormater;

/**
 * Classe représentative de la table des notifications
 * <p/>
 * Created by ThomasHiron on 11/05/2015.
 */
public class Image
{
    private int id;
    private Date date;
    private Point point;

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
        date = DateFormater.getDateFromString(pDate);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Point getPoint()
    {
        return point;
    }

    public void setPoint(Point point)
    {
        this.point = point;
    }
}
