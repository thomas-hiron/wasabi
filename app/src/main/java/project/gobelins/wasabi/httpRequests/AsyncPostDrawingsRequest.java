package project.gobelins.wasabi.httpRequests;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.sqlite.tables.Drawings;

/**
 * Created by ThomasHiron on 27/04/2015.
 */
public class AsyncPostDrawingsRequest extends AsyncPostRequests
{
    private Context mContext;

    public AsyncPostDrawingsRequest(List<NameValuePair> nameValuePairs, Wasabi wasabi)
    {
        super(nameValuePairs);

        mContext = wasabi.getApplicationContext();
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);

        /* Json decode */
        try
        {
            JSONObject jsonObject = new JSONObject(s);

            /* Chaque date */
            JSONObject pointsObject = (JSONObject) jsonObject.get("points");
            Iterator<String> keys = pointsObject.keys();

            while(keys.hasNext())
            {
                String currentDate = keys.next();
                JSONArray dateArray = (JSONArray) pointsObject.get(currentDate);

                /* Enregistrement de chaque traits */
                for(int i = 0, l = dateArray.length(); i < l; ++i)
                {
                    JSONObject currentDrawing = (JSONObject) dateArray.get(i);
                    String points = (String) currentDrawing.get("points");
                    String color = (String) currentDrawing.get("color");

                    ContentValues contentValues = new ContentValues(4);
                    contentValues.put(Drawings.DRAWINGS_POINTS, points.replaceAll("\\[(.*)\\]", "$1"));
                    contentValues.put(Drawings.DRAWINGS_DATE, currentDate);
                    contentValues.put(Drawings.DRAWINGS_COLOR, Color.parseColor(color));
                    contentValues.put(Drawings.DRAWINGS_ACCOMPLICE, 1);

                    /* Enregistrement */
                    mContext.getContentResolver().insert(Uri.parse(Drawings.URL_DRAWINGS), contentValues);
                }
            }


        }
        catch(NullPointerException | JSONException e)
        {
            e.printStackTrace();
        }
    }
}
