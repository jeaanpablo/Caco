package br.com.caco.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.caco.database.UserData;
import br.com.caco.model.Notification;
import br.com.caco.model.User;

/**
 * Created by Jean Pablo Bosso on 02/12/2015.
 */
public class NotificationDAO {

    private UserData db;


    public NotificationDAO (Context ctx)
    {
        this.db = new UserData(ctx);
    }


    public void insertNotification(Notification notif)
    {
        SQLiteDatabase database = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_user_requester", notif.getIdUserRequester());
        values.put("name_user_requester", notif.getNameUserRequester());
        values.put("img_path", notif.getImgPath());
        values.put("notif_type", notif.getType());
        values.put("id_store", notif.getIdStore());
        values.put("store_name", notif.getNameStore());


        database.insert(UserData.TABLE_NAME_NOTIFICATION, null, values);
        database.close();
    }

    public List<Notification> getAll()
    {
        List<Notification> notifications = new ArrayList<Notification>();
        SQLiteDatabase database = this.db.getReadableDatabase();
        String[] FROM = {"_id", "id_user_requester", "name_user_requester", "img_path", "notif_type", "id_store", "store_name", " id_approver integer"};

        Cursor cursor = database.query(UserData.TABLE_NAME_NOTIFICATION, FROM, null, null, null, null, null);

        cursor.isFirst();

        while(cursor.moveToNext())
        {
            Notification notif = new Notification();


            notif.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            notif.setIdUserRequester(cursor.getInt(cursor.getColumnIndex("id_user_requester")));
            notif.setNameUserRequester(cursor.getString(cursor.getColumnIndex("name_user_requester")));
            notif.setImgPath(cursor.getString(cursor.getColumnIndex("img_path")));
            notif.setType(cursor.getString(cursor.getColumnIndex("notif_type")));
            notif.setIdStore(cursor.getInt(cursor.getColumnIndex("id_store")));
            notif.setNameStore(cursor.getString(cursor.getColumnIndex("store_name")));
            notif.setIdUserApprover(cursor.getColumnIndex("id_approver"));


            database.close();

            notifications.add(notif);
        }


        return notifications;

    }

}
