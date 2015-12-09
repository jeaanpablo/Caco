package br.com.caco.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.caco.R;
import br.com.caco.adapters.FriendsAdapter;
import br.com.caco.adapters.NotificationsAdapter;
import br.com.caco.adapters.RecentlyUsedFidelityCardListItemAdapter;
import br.com.caco.database.dao.FriendDAO;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.model.Friend;
import br.com.caco.model.LoyalityCard;
import br.com.caco.model.Notification;
import br.com.caco.model.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class FriendsActivity extends Activity{

    UserDAO userDao;
    FriendDAO friendDao;

    List<User> userList;

    String query = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 handleIntent(getIntent());
		 setContentView(R.layout.activity_friends);

        userDao  = new UserDAO(this);

       userList = userDao.getAll();


        inflateList(this, userList.get(0), query);


		 
		super.onCreate(savedInstanceState);
	}
	

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);

      //  inflateList(this, userList.get(0), query);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.friends_menu, menu);
	    // Associate searchable configuration with the SearchView

        if(query == null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }
        else
        {
            menu.getItem(0).setVisible(false);
        }

	    return true;
	}


	public void inflateList(final Context context, final User user, final String word)
	{


		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			public ProgressDialog mProgressDialog;
			List<Friend> list = new ArrayList<Friend>();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setMessage("Por favor espere...");
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

                friendDao = new FriendDAO(context);
                if(query != null) {
                    list = getFriendByUser(user, word);
                    Collections.reverse(list);



                    if (query != null) {
                        List<Friend> myFriends = friendDao.getAll();


                        for (int i = 0; i < myFriends.size(); i++) {
                            Friend friend = myFriends.get(i);

                            for (int j = 0; j < list.size(); j++) {
                                Friend estranger = list.get(j);

                                if (friend.getIdUser() == estranger.getIdUser()) {
                                    list.get(j).setAdd(true);
                                }

                            }

                        }

                    }
                }
                else
                {
                    list = friendDao.getAll();

                    for(int i =0 ; i < list.size(); i++)
                    {
                        list.get(i).setBitmapImg(getBitmapFromURL(list.get(i).getImage()));
                    }

                }


                return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);

                FriendsAdapter adapter = new FriendsAdapter (context, list);
                ListView listView = (ListView) findViewById(R.id.listFriends);
                listView.setAdapter(adapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


					}
				});

				mProgressDialog.dismiss();


			}

		}.execute();
	}

	public List<Friend> getFriendByUser(User user, String word) {
		// Create a new HttpClient and Post Header

		List<Friend> list = new ArrayList<Friend>();
		String url = null;

		if(word == null)
		{
			url = "http://45.79.178.168:8080/Caco-webservice/getFriendsByUserId";
		}
		else
		{
			url = "http://45.79.178.168:8080/Caco-webservice/searchAllClients";
		}

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if(word == null) {
				nameValuePairs.add(new BasicNameValuePair("id_user", "" + user.getId()));
				nameValuePairs.add(new BasicNameValuePair("token", user.getToken()));
			} else
			{
				nameValuePairs.add(new BasicNameValuePair("word", "" + word));
			}


			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}


			JSONArray jsonArray = new JSONArray(builder.toString());

			for(int i=0;i<jsonArray.length();i++) {
				JSONObject lines = (JSONObject) new JSONTokener(jsonArray.getString(i)).nextValue();
				Friend friend = new Friend();

                friend.setName(lines.getString("name"));
				friend.setIdUser(lines.getInt("idUser"));
                friend.setBitmapImg(getBitmapFromURL(lines.getString("imgUrl")));

               if(word == null) {
                   friend.setAdd(true);
               }
                else
               {
                   friend.setAdd(false);
               }


				list.add(friend);

			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			user = null;
		} catch (IOException e) {
			e.printStackTrace();
			user = null;
		} catch (JSONException e) {
			e.printStackTrace();
			user = null;
		}

		return list;
	}


	public static Bitmap getBitmapFromURL(String link) {
    /*--- this method downloads an Image from the given URL,
     *  then decodes and returns a Bitmap object
     ---*/
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("getBmpFromUrl error: ", e.getMessage().toString());
			return null;
		}
	}


	public void updateProfilePicture(final Context context, final User user)
	{


		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			public ProgressDialog mProgressDialog;
			List<Friend> list = new ArrayList<Friend>();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setMessage("Por favor espere...");
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

	//			updateProfileImageUser(user);

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);

				mProgressDialog.dismiss();


			}

		}.execute();
	}

    /*
	public void searcheUser(String word) {
		// Create a new HttpClient and Post Header

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://45.79.178.168:8080/Caco-webservice/updateUserImageProfile");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("image", ""+user.getImage()));
			nameValuePairs.add(new BasicNameValuePair("image_name", user.getImageName()));
			nameValuePairs.add(new BasicNameValuePair("id_user", "" + user.getId()));


			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			String string = response.getStatusLine().getReasonPhrase();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			user = null;
		} catch (IOException e) {
			e.printStackTrace();
			user = null;
		}
	}
	private String getPath(Uri uri) {
		if( uri == null ) {
			return null;
		}

		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor;
		if(Build.VERSION.SDK_INT >19)
		{
			// Will return "image:x*"
			String wholeID = DocumentsContract.getDocumentId(uri);
			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];
			// where id is equal to
			String sel = MediaStore.Images.Media._ID + "=?";

			cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					projection, sel, new String[]{ id }, null);
		}
		else
		{
			cursor = getContentResolver().query(uri, projection, null, null, null);
		}
		String path = null;
		try
		{
			int column_index = cursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index).toString();
			cursor.close();
		}
		catch(NullPointerException e) {

		}
		return path;
	}

*/


}
