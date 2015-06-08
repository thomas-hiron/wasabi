package project.gobelins.wasabi.httpRequests;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import project.gobelins.wasabi.notifications.views.ImageView;

/**
 * Created by ThomasHiron on 27/04/2015.
 */
public class AsyncPostImageRequest extends AsyncPostRequests
{
    private ImageView mImageView;

    public AsyncPostImageRequest(List<NameValuePair> nameValuePairs, ImageView imageView)
    {
        super(nameValuePairs);

        mImageView = imageView;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);

        /* Json decode */
        try
        {
            JSONObject jsonObject = new JSONObject(s);

            String imageUrl = jsonObject.getString("image_url");

            mImageView.imageSuccess(imageUrl);
        }
        catch(NullPointerException | JSONException e)
        {
            e.printStackTrace();
            mImageView.imageError();
        }
    }
}
