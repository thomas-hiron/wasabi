package project.gobelins.wasabi.httpRequests;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

            int deviceWidth = Wasabi.SCREEN_WIDTH;
            int deviceHeight = Wasabi.SCREEN_HEIGHT;

            while(keys.hasNext())
            {
                String currentDate = keys.next();
                JSONArray dateArray = (JSONArray) pointsObject.get(currentDate);

                /* Enregistrement de chaque traits */
                for(int i = 0, l = dateArray.length(); i < l; ++i)
                {
                    /* Récupération des valeurs */
                    JSONObject currentDrawing = (JSONObject) dateArray.get(i);
                    String points = ((String) currentDrawing.get("points")).replaceAll("\\[(.*)\\]", "$1");
                    String color = (String) currentDrawing.get("color");
                    String screen = (String) currentDrawing.get("screen");
                    String[] split = screen.split(",");

                    int width = Integer.parseInt(split[0]);
                    int height = Integer.parseInt(split[1]);

                    /* Changement du ratio des points */
                    String[] pointsArray = points.split(",");
                    ArrayList<Integer> pointsOutput = new ArrayList<>(pointsArray.length);
                    for(int j = 0, k = pointsArray.length; j < k; j += 2)
                    {
                        pointsOutput.add(Integer.parseInt(pointsArray[j]) * deviceWidth / width);
                        pointsOutput.add(Integer.parseInt(pointsArray[j+1]) * deviceHeight / height);
                    }

                    ContentValues contentValues = new ContentValues(4);
                    contentValues.put(Drawings.DRAWINGS_POINTS, TextUtils.join(",", pointsOutput));
                    contentValues.put(Drawings.DRAWINGS_DATE, "2015-04-26");
                    contentValues.put(Drawings.DRAWINGS_COLOR, Color.parseColor(color));
                    contentValues.put(Drawings.DRAWINGS_ACCOMPLICE, 1);

                    /* Enregistrement */
                    mContext.getContentResolver().insert(Uri.parse(Drawings.URL_DRAWINGS), contentValues);
                }
            }
        }
        catch(NullPointerException | JSONException | ClassCastException e)
        {
            e.printStackTrace();
        }
    }
}
