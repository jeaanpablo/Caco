package br.com.caco.gui;


import br.com.caco.R;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.model.User;
import br.com.caco.util.Validation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

                if((email == Boolean.TRUE) && (senha == Boolean.TRUE))
                {
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
		} );
		
	}

    public void executeLogin(final Context context, final User user)
    {


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

                User newUser= postData(user);

                if(newUser != null)
                {
                    UserDAO userDAO = new UserDAO(context);
                    userDAO.insertUser(newUser);
                    startNewActivity = true;
                }

                if(newUser == null)
                {
                    startNewActivity = false;
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

                    Toast.makeText(context, "Não foi possivel conectar-se ao servidor", Toast.LENGTH_LONG).show();
                }


            }

        }.execute();
    }


    public User postData(User user) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.154.172.30:8080/Caco-webservice/userLogin");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", user.getEmail()));
            nameValuePairs.add(new BasicNameValuePair("login", user.getLogin()));
            nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            JSONObject jsonObject = new JSONObject(builder.toString());

            user.setId(jsonObject.optInt("id"));
            user.setToken(jsonObject.optString("token"));
            user.setPermission(jsonObject.optString("permission"));

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




}
