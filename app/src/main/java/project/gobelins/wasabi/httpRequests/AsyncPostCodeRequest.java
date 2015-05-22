package project.gobelins.wasabi.httpRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import project.gobelins.wasabi.Wasabi;
import project.gobelins.wasabi.views.FormCode;

/**
 * Created by ThomasHiron on 27/04/2015.
 */
public class AsyncPostCodeRequest extends AsyncPostRequests
{
    private FormCode mForm;
    private Context mContext;

    public AsyncPostCodeRequest(List<NameValuePair> nameValuePairs, FormCode form)
    {
        super(nameValuePairs);

        mForm = form;
        mContext = mForm.getContext();
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);

        /* Json decode */
        try
        {
            JSONObject jsonObject = new JSONObject(s);

            boolean success = jsonObject.getBoolean("code");

            /* Affichage d'un message d'erreur si échec */
            if(!success)
                mForm.error();
            /* Sinon on enregistre la clé dans les sharedPreferences */
            else
            {
                String api_key = jsonObject.getString("api_key");

                SharedPreferences prefs = mContext.getSharedPreferences(
                        Wasabi.class.getSimpleName(),
                        Context.MODE_PRIVATE
                );
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Wasabi.API_KEY, api_key);
                editor.apply();
            }
        }
        catch(NullPointerException | JSONException e)
        {
            mForm.error();
            e.printStackTrace();
        }
    }
}
