package project.gobelins.wasabi.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import project.gobelins.wasabi.utils.DateFormater;

/**
 * Created by ThomasHiron on 28/04/2015.
 */
public class Notification implements Parcelable
{
    private int id;
    private boolean read;
    private int type;
    private Date date;

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

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public boolean isRead()
    {
        return read;
    }

    public void setRead(boolean read)
    {
        this.read = read;
    }

    public void setRead(int read)
    {
        this.read = read == 1;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>()
    {
        public Notification createFromParcel(Parcel in)
        {
            return new Notification(in);
        }

        public Notification[] newArray(int size)
        {
            return new Notification[size];
        }
    };

    private Notification(Parcel in)
    {
        setId(in.readInt());
        setRead(in.readInt());
        setType(in.readInt());

        /* La date */
        String dateString = in.readString();
        setDate(DateFormater.getDateFromString(dateString));
    }

    public Notification()
    {

    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(getId());
        parcel.writeInt(isRead() ? 1 : 0);
        parcel.writeInt(getType());
        parcel.writeString(getDate().toString());
    }
}
