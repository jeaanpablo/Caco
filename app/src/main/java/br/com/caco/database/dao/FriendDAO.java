package br.com.caco.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.caco.database.UserData;
import br.com.caco.model.Friend;
import br.com.caco.model.Notification;

/**
 * Created by Jean Pablo Bosso on 07/12/2015.
 */
public class FriendDAO {


    private UserData db;


    public FriendDAO (Context ctx)
    {
        this.db = new UserData(ctx);
    }


    public void insertFriend(Friend friend)
    {
        SQLiteDatabase database = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_user", friend.getIdUser());
        values.put("name", friend.getName());
        values.put("img_path", friend.getImage());

        database.insert(UserData.TABLE_NAME_FRIENDS, null, values);
        database.close();
    }

    public List<Friend> getAll()
    {
        List<Friend> friends = new ArrayList<Friend>();
        SQLiteDatabase database = this.db.getReadableDatabase();
        String[] FROM = {"_id", "id_user", "name", "img_path"};

        Cursor cursor = database.query(UserData.TABLE_NAME_FRIENDS, FROM, null, null, null, null, null);

        cursor.isFirst();

        while(cursor.moveToNext())
        {
            Friend friend = new Friend();


            friend.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            friend.setIdUser(cursor.getInt(cursor.getColumnIndex("id_user")));
            friend.setName(cursor.getString(cursor.getColumnIndex("name")));
            friend.setImage(cursor.getString(cursor.getColumnIndex("img_path")));



            database.close();
            friends.add(friend);
        }


        return friends;

    }


    public void deleteNotification(Friend friend)
    {
        SQLiteDatabase database = db.getWritableDatabase();
        String where = "_id="+friend.getId();
        database.delete(UserData.TABLE_NAME_FRIENDS, where, null);
        database.close();
    }


    public void deleteAll()
    {
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(UserData.TABLE_NAME_FRIENDS, null, null);
        database.close();
    }






}
