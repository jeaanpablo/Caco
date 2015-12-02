package br.com.caco.gcm;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.IOException;

import br.com.caco.R;

/**
 * Created by Jean Pablo Bosso on 29/11/2015.
 */
public class GcmIDListnerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Context context =  getApplicationContext();
        InstanceID instanceID = InstanceID.getInstance(context);
        try {
            String token = instanceID.getToken(context.getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //send token to app server
    }
}