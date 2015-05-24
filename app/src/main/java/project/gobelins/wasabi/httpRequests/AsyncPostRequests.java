package project.gobelins.wasabi.httpRequests;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by ThomasHiron on 27/04/2015.
 */
public class AsyncPostRequests extends AsyncTask<String, String, String>
{
    /* Les données POST */
    private List<NameValuePair> mNameValuePairs;

    public AsyncPostRequests(List<NameValuePair> nameValuePairs)
    {
        mNameValuePairs = nameValuePairs;
    }

    @Override
    protected String doInBackground(String... args)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(args[0]);
        HttpResponse response;
        String responseString = null;

        try
        {
            /* Ajout des données */
            httpPost.setEntity(new UrlEncodedFormEntity(mNameValuePairs));

            response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK)
            {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            }
            else
            {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return responseString;
    }
}
