package br.com.caco.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import java.lang.reflect.Array;
import java.util.List;
import java.util.StringTokenizer;

import br.com.caco.R;
import br.com.caco.database.dao.NotificationDAO;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.model.Notification;
import br.com.caco.model.User;

/**
 * Created by Jean Pablo Bosso on 29/11/2015.
 */

public class GcmIntentService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String[] splitMesage;

            NotificationDAO notificationDAO = new NotificationDAO(getApplicationContext());
        UserDAO userDAO = new UserDAO(getApplicationContext());
        List<User> users = userDAO.getAll();
        User user = users.get(0);

        StringTokenizer tokens = new StringTokenizer(message, ";");

            String type = tokens.nextToken();

            if(type.equalsIgnoreCase("FRIEND"))
            {

                String idFriend = tokens.nextToken();

                String friendName = tokens.nextToken();

                generateNoification(friendName, "Quer ser seu amigo.");

                Notification notification = new Notification();

                notification.setIdUserRequester(Integer.parseInt(idFriend));
                notification.setNameUserRequester(friendName);
                notification.setType(type);
                notification.setIdUserApprover(user.getId());

                notificationDAO.insertNotification(notification);

                updateMyActivity(getApplicationContext(), message);

            }


        if(type.equalsIgnoreCase("STORE"))
        {

            String idFriend = tokens.nextToken();

            String friendName = tokens.nextToken();

            String idStore = tokens.nextToken();

            String storeName = tokens.nextToken();


            Notification notification = new Notification();

            generateNoification(friendName, "Indicou " + storeName + " para você.");

            notification.setIdUserRequester(Integer.parseInt(idFriend));
            notification.setNameUserRequester(friendName);
            notification.setIdStore(Integer.parseInt(idStore));
            notification.setNameStore(storeName);
            notification.setType(type);

            notificationDAO.insertNotification(notification);

            updateMyActivity(getApplicationContext(), message);

        }



    }

    public void generateNoification (String title, String message)
    {

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.caco_logo)
                .setContentTitle(title)
                .setContentText(message);
        notificationManager.notify(1, mBuilder.build());

    }

    static void updateMyActivity(Context context, String message) {

        Intent data = new Intent("bcNewMessage");
        data.putExtra("message", message);
        context.sendBroadcast(data);
    }


}