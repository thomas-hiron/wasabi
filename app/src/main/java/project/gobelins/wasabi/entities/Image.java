package project.gobelins.wasabi.entities;

import java.util.Date;

import project.gobelins.wasabi.fresco.drawing.Point;
import project.gobelins.wasabi.utils.DateFormater;

/**
 * Classe représentative de la table des notifications
 * <p/>
 * Created by ThomasHiron on 11/05/2015.
 */
public class Image extends Entity
{
    private int id;
    private Date date;
    private Point point;
    private String fileName;

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

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

    public void setPoint(String string)
    {
        String[] coords = string.split(",");
        Point point = new Point();
        point.set(Float.valueOf(coords[0]), Float.valueOf(coords[1]));

        setPoint(point);
    }
}
