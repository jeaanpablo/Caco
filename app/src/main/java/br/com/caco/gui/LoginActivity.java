package br.com.caco.gui;


import br.com.caco.R;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.model.User;
import br.com.caco.util.Util;
import br.com.caco.util.Validation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends FragmentActivity{

    UserDAO userDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        userDao  = new UserDAO(getApplicationContext());

        List<User> userList = userDao.getAll();


        if(userList.isEmpty()) {
            setContentView(R.layout.activity_login);

            Button btnCadastrese = (Button) findViewById(R.id.buttonCadastreSe);
            Button btnEntrar = (Button) findViewById(R.id.buttonLoginEntrar);

            btnEntrar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    boolean email, senha;

                    EditText editEmail = (EditText) findViewById(R.id.editTextLoginEmail);
                    EditText editSenha = (EditText) findViewById(R.id.editTextLoginSenha);

                    email = Validation.isEmailAddress(editEmail, true);
                    senha = Validation.hasText(editSenha);

                    if ((email == Boolean.TRUE) && (senha == Boolean.TRUE)) {
                        User user = new User();
                        user.setEmail(editEmail.getText().toString());
                        user.setLogin("");
                        user.setPassword(editSenha.getText().toString());

                        executeLogin(v.getContext(), user);
                    }


                }
            });

            btnCadastrese.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent it = new Intent(getApplicationContext(), CreateAccountActivity.class);
                    startActivity(it);
                }
            });
        }else
        {
            Intent itMain = new Intent (getApplicationContext(), FidelityCardActivity.class);
            startActivity(itMain);
            finish();
        }
		
	}

    public void executeLogin(final Context context, final User user)
    {


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

                user.setRedId(Util.getRegId(context));

                User newUser= postData(user);

                if(newUser.getId() != 0)
                {
                    UserDAO userDAO = new UserDAO(context);

                    Bitmap bitmap = getBitmapFromURL(newUser.getImagePath());

                    try {
                        user.setImagePath(saveToInternalSorage(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    userDAO.insertUser(newUser);
                    startNewActivity = true;
                }

                if(newUser.getId() == 0)
                {
                    startNewActivity = false;
                    message = newUser.getToken();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                mProgressDialog.dismiss();

                if(startNewActivity == Boolean.TRUE) {
                    Intent itMain = new Intent (context, FidelityCardActivity.class);
                    startActivity(itMain);
                    finish();
                }
                else
                {

                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }


            }

        }.execute();
    }


    public User postData(User user) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://45.79.178.168:8080/Caco-webservice/userAppLogin");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", user.getEmail()));
            nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
            nameValuePairs.add(new BasicNameValuePair("regId", user.getRedId()));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);


            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }
                JSONObject jsonObject = new JSONObject(builder.toString());

                user.setId(jsonObject.optInt("idUser"));
                user.setToken(jsonObject.optString("token"));
                user.setPermission(jsonObject.optString("permission"));
                user.setFirstName(jsonObject.optString("firstName"));
                user.setLastName(jsonObject.optString("lastName"));
                user.setCellphone(jsonObject.optInt("cellphone"));
                user.setImagePath(jsonObject.optString("imgUrl"));

            }
            else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND)
            {
                String responseString = EntityUtils.toString(response.getEntity());
                String[] partOne = responseString.split("<h1>");
                String[] partTwo = partOne[1].split("</h1>");
                String[] partThree = partTwo[0].split(" - ");

                user.setToken(partThree[1]);
                user.setId(0);
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

        return user;
    }


    private String saveToInternalSorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return mypath.getAbsolutePath();
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
