package br.com.caco.gui;

import br.com.caco.R;
import br.com.caco.adapters.FidelityCardTabPagerAdapter;
import br.com.caco.database.dao.NotificationDAO;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.model.User;
import br.com.caco.util.Util;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FidelityCardActivity extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private ActionBar actionBar;
    private FidelityCardTabPagerAdapter tabPagerAdapter;
    private String[] tabs = {"Recentily Used Cards", "Geolocation Cards"};
    private UserDAO userDAO;
    private NotificationDAO notificationDAO;
    private Context ctx;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fidelity_cards_view_pager);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabPagerAdapter = new FidelityCardTabPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabPagerAdapter);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.white)));

        ctx = getApplication();


        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.calendar).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.geolocation).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.store).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.world).setTabListener(this));


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * on swipe select the respective tab
             */
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());

        if (tab.getPosition() == 0) {
            getActionBar().setTitle("Utilizados Recentemente");
        }

        if (tab.getPosition() == 1) {
            getActionBar().setTitle("Mais Próximos");
        }

        if (tab.getPosition() == 2) {
            getActionBar().setTitle("Lojas");
        }

        if (tab.getPosition() == 3) {
            getActionBar().setTitle("Notificações");
        }


    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_profile) {
            Intent itMain = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(itMain);
        }

        if (item.getItemId() == R.id.action_friends) {
            Intent itMain = new Intent(getApplicationContext(), FriendsActivity.class);
            startActivity(itMain);
        }

        if (item.getItemId() == R.id.action_logout) {
            doLogout(this);
        }


        return super.onOptionsItemSelected(item);
    }


    public void doLogout(final Context context){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            public boolean startNewActivity;
            public ProgressDialog mProgressDialog;
            String message = "Não foi possivel conectar-se ao servidor";

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

                notificationDAO = new NotificationDAO(context);
                userDAO = new UserDAO(context);

                List<User> list = userDAO.getAll();

                postData(list.get(0));

                userDAO.deleteAll();
                notificationDAO.deleteAll();

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                mProgressDialog.dismiss();

                Intent itMain = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(itMain);
                finish();


            }

        }.execute();

    }


    public void postData(User user) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://45.79.178.168:8080/Caco-webservice/appLogout");

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