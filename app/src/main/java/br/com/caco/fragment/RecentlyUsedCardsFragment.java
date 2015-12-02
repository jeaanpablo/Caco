package br.com.caco.fragment;

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
import br.com.caco.adapters.AllStoresAdapter;
import br.com.caco.adapters.RecentlyUsedFidelityCardListItemAdapter;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.gui.StoreProfileActivity;
import br.com.caco.model.LoyalityCard;
import br.com.caco.model.Store;
import br.com.caco.model.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class RecentlyUsedCardsFragment extends Fragment {

    private String tab;
    private int color;
    UserDAO userDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
			
        View view = inflater.inflate(R.layout.fragment_rencetly_used_fidelity_cards, null);


        userDao  = new UserDAO(view.getContext());

        List<User> userList = userDao.getAll();

        inflateLoyalityList(view, userList.get(0));


        return view;
    }


    public void inflateLoyalityList(final View view, final User user)
    {


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            public ProgressDialog mProgressDialog;
            List<LoyalityCard> list = new ArrayList<LoyalityCard>();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mProgressDialog = new ProgressDialog(view.getContext());
                mProgressDialog.setMessage("Por favor espere...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                list = getLotalityCardByUser(user);
                Collections.reverse(list);

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                final RecentlyUsedFidelityCardListItemAdapter adapter = new RecentlyUsedFidelityCardListItemAdapter(view.getContext(), list);
                ListView listView = (ListView) view.findViewById(R.id.listFidelityCardsRecentlyUsed);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent itMain = new Intent(view.getContext(), StoreProfileActivity.class);
                        startActivity(itMain);


                    }
                });

                mProgressDialog.dismiss();


            }

        }.execute();
    }

    public List<LoyalityCard> getLotalityCardByUser(User user) {
        // Create a new HttpClient and Post Header

        List<LoyalityCard> list = new ArrayList<LoyalityCard>();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://45.79.178.168:8080/Caco-webservice/getLoyalityCardsByUser");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_user", ""+user.getId()));
            nameValuePairs.add(new BasicNameValuePair("token", user.getToken()));


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
                LoyalityCard loyality = new LoyalityCard();
                Calendar cal = Calendar.getInstance();

                loyality.setStoreName(lines.getString("storeName"));
                loyality.setPoints("" + lines.getLong("points"));
                loyality.setStoreLogo(getBitmapFromURL(lines.getString("urlImage")));

                cal.setTimeInMillis(lines.getLong("lastUse"));

                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                System.out.println(cal.getTime());

                String formatted = format1.format(cal.getTime());


                loyality.setData(formatted);


                list.add(loyality);

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


}