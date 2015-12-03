
	
	package br.com.caco.adapters;

	import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.util.ArrayList;
    import java.util.List;

	import br.com.caco.R;
    import br.com.caco.database.dao.UserDAO;
    import br.com.caco.gui.FidelityCardActivity;
    import br.com.caco.model.Notification;
import br.com.caco.model.Store;
    import br.com.caco.model.User;
    import br.com.caco.util.Util;

    import android.annotation.SuppressLint;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.Intent;
    import android.os.AsyncTask;
    import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
    import android.widget.Button;
    import android.widget.ImageView;
import android.widget.TextView;
    import android.widget.Toast;

    import org.apache.http.HttpResponse;
    import org.apache.http.NameValuePair;
    import org.apache.http.client.ClientProtocolException;
    import org.apache.http.client.HttpClient;
    import org.apache.http.client.entity.UrlEncodedFormEntity;
    import org.apache.http.client.methods.HttpPost;
    import org.apache.http.impl.client.DefaultHttpClient;
    import org.apache.http.message.BasicNameValuePair;
    import org.json.JSONException;
    import org.json.JSONObject;

    public class NotificationsAdapter extends BaseAdapter {

        private Context context;
        private List<Notification> notificationList;

        public NotificationsAdapter(Context context,
                                    List<Notification> notificationList) {
            this.context = context;
            this.notificationList = notificationList;
        }

        @Override
        public int getCount() {

            return notificationList.size();
        }

        @Override
        public Object getItem(int position) {

            return notificationList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return notificationList.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Notification item = notificationList.get(position);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater
                    .inflate(R.layout.list_notifications_item, null);
            TextView textNotificationTitle = (TextView) v
                    .findViewById(R.id.textViewNotificationTitle);
            TextView textNotificationMessage = (TextView) v
                    .findViewById(R.id.textViewNotificationsMessage);
            ImageView imageNotification = (ImageView) v
                    .findViewById(R.id.imageViewNotificationImage);
            Button btnAccept = (Button) v.findViewById(R.id.buttonAccept);
            Button btnRecuse = (Button) v.findViewById(R.id.buttonRecuse);
            Button btnConfirm = (Button) v.findViewById(R.id.buttonConfirm);


            if (item.getType().equals("FRIEND")) {
                textNotificationTitle.setText(item.getNameUserRequester());
                textNotificationMessage.setText("Quer ser seu amigo.");
                imageNotification.setImageResource(R.drawable.goku);

                btnAccept.setVisibility(View.VISIBLE);
                btnRecuse.setVisibility(View.VISIBLE);

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        item.setStatusNotification(2);

                        sendResponse(v.getContext(), item);

                    }
                });


            } else if (item.getType().equals("STORE")) {

                textNotificationTitle.setText(item.getNameUserRequester());
                textNotificationMessage.setText("Indicou " + item.getNameStore() + " para você.");
                imageNotification.setImageResource(R.drawable.goku);

                btnAccept.setVisibility(View.VISIBLE);
                btnRecuse.setVisibility(View.VISIBLE);

            }


            return v;
        }


        public void sendResponse(final Context context, final Notification notification) {


            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                public boolean startNewActivity;
                public ProgressDialog mProgressDialog;

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

                    postData(notification);


                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);

                    mProgressDialog.dismiss();


                    Toast.makeText(context, "Não foi possivel conectar-se ao servidor", Toast.LENGTH_LONG).show();


                }

            }.execute();
        }


        public boolean postData(Notification notification) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://45.79.178.168:8080/Caco-webservice/responseNotification");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id_user", "" + notification.getIdUserApprover()));
                nameValuePairs.add(new BasicNameValuePair("type", notification.getType()));
                nameValuePairs.add(new BasicNameValuePair("status", "" + notification.getStatusNotification()));
                nameValuePairs.add(new BasicNameValuePair("id_user_friend", "" + notification.getNameUserRequester()));
                nameValuePairs.add(new BasicNameValuePair("id_store", "" + notification.getIdStore()));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }

                String massa = builder.toString();


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                notification = null;
            } catch (IOException e) {
                e.printStackTrace();
                notification = null;

                return true;
            }

            return true;
        }
    }
