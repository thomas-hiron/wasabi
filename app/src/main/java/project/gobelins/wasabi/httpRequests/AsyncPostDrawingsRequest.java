package project.gobelins.wasabi.httpRequests;

import android.content.Context;

import org.apache.http.NameValuePair;

import java.util.List;

import project.gobelins.wasabi.Wasabi;

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
    }
}
