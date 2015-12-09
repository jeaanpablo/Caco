package br.com.caco.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.caco.database.UserData;
import br.com.caco.model.User;

/**
 * Created by Jean Pablo Bosso on 23/06/2015.
 */
public class  UserDAO {


    private UserData db;


    public UserDAO (Context ctx)
    {
        this.db = new UserData(ctx);
    }


    public void insertUser(User user)
    {
        SQLiteDatabase database = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("token", user.getToken());
        values.put("permission", user.getPermission());
        values.put("id_user", user.getId());
        values.put("first_name", user.getFirstName());
        values.put("last_name", user.getLastName());
        values.put("cellphone", user.getCellphone());
        values.put("img_path", user.getImagePath());

        database.insert(UserData.TABLE_NAME_USER, null, values);
        database.close();
    }

    public List<User> getAll()
    {
        List<User> users = new ArrayList<User>();
        SQLiteDatabase database = this.db.getReadableDatabase();
        String[] FROM = {"_id", "token", "permission", "id_user", "first_name", "last_name", "img_path", "cellphone"};

        Cursor cursor = database.query(UserData.TABLE_NAME_USER, FROM, null, null, null, null, null);

        cursor.isFirst();

        while(cursor.moveToNext())
        {
            User user = new User();


            user.setToken(cursor.getString(cursor.getColumnIndex("token")));
            user.setPermission(cursor.getString(cursor.getColumnIndex("permission")));
            user.setId(cursor.getInt(cursor.getColumnIndex("id_user")));
            user.setImagePath(cursor.getString(cursor.getColumnIndex("img_path")));
            user.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
            user.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
            user.setCellphone(cursor.getLong(cursor.getColumnIndex("cellphone")));

            database.close();

           users.add(user);
        }


        return users;

    }


    public void deleteAll()
    {
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(UserData.TABLE_NAME_USER, null, null);
        database.close();
    }

/*
    public void updateMonitoring (Monitoring monitoring)
    {
        SQLiteDatabase database = db.getWritableDatabase();
        String where = "url='"+monitoring.getUrl()+"'";
        ContentValues values = new ContentValues();

//		int responseTime =  Integer.parseInt(monitoring.getResponseTime());

        values.put("status", monitoring.isStatus());
        values.put("responseTime",monitoring.getJsonResponseTime());
        values.put("message", monitoring.getMessageResponse());
        values.put("responseCode", monitoring.getResponseCode());
        values.put("date", monitoring.getJsonDate());
        values.put("notification", monitoring.isNotification());
        values.put("countTime", monitoring.getCountTime());
        values.put("window", monitoring.getWindow());
        //values.put("notification_control", monitoring.isNotificationControl());

        database.update(MonitoringData.TABLE_NAME, values, where, null);
        database.close();

    }

    public void updateNotificationControl (Monitoring monitoring)
    {
        SQLiteDatabase database = db.getWritableDatabase();
        String where = "url='"+monitoring.getUrl()+"'";
        ContentValues values = new ContentValues();

        values.put("notification_control", monitoring.isNotificationControl());

        database.update(MonitoringData.TABLE_NAME, values, where, null);
        database.close();

    }



    public void deleteMonitoring(Monitoring monitoring)
    {
        SQLiteDatabase database = db.getWritableDatabase();
        String where = "url='"+monitoring.getUrl()+"'";
        database.delete(MonitoringData.TABLE_NAME, where, null);
        database.close();
    }
    */



}
