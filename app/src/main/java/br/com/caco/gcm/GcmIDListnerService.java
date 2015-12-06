package br.com.caco.gcm;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.com.caco.R;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.model.User;

/**
 * Created by Jean Pablo Bosso on 29/11/2015.
 */
public class GcmIDListnerService extends InstanceIDListenerService {

    UserDAO userDao;

    @Override
    public void onTokenRefresh() {
        Context context = getApplicationContext();

        userDao = new UserDAO(context);

        List<User> user = userDao.getAll();


        InstanceID instanceID = InstanceID.getInstance(context);


        try {
            String token = instanceID.getToken(context.getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            user.get(0).setRedId(token);
            postData(user.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //send token to app server
    }


    public void postData(User user) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://45.79.178.168:8080/Caco-webservice/updateAndroidRegId");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_user", "" + user.getId()));
            nameValuePairs.add(new BasicNameValuePair("regId", user.getRedId()));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}