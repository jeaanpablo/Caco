package br.com.caco.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.caco.R;
import br.com.caco.model.User;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class CreateAccountActivity extends Activity{
	String gender;
	private static final String TAG = "HttpClient";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		getActionBar().setTitle(R.string.login_criarconta);
		
		final EditText editEmail = (EditText) findViewById(R.id.editTextCreateAccountEmail);
		Button btnCriar = (Button) findViewById(R.id.buttonCreateAccountEntrar);
		
		String mail ;
		
		mail = getEmail(this, "com.facebook.auth.login");
		
		if(mail != null)
		{
			editEmail.setText(mail);
		}
		else
		{
			mail = getEmail(this, "com.google");
					editEmail.setText(mail);
		}	
		
		btnCriar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*
				EditText editNome = (EditText) findViewById(R.id.editTextCreateAccountNome);
				EditText editCelular= (EditText) findViewById(R.id.editTextCreateAccountCelular);
				EditText editSenha = (EditText) findViewById(R.id.editTextCreateAccountSenha);
				EditText editAniversario = (EditText) findViewById(R.id.editTextCreateAccountAniversario);

				User user = new User();
				
				user.setCompleteName(editNome.getText().toString());
				user.setCellphone(editCelular.getText().toString());
				user.setEmail(editEmail.getText().toString());
				user.setGender(gender);
				user.setBirthdate(editAniversario.getText().toString());
				
				try {
					createUser(user);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
						Intent itMain = new Intent (getApplicationContext(), FidelityCardActivity.class);
						startActivity(itMain);
	
				
				
			}
		});
		
		
		
		
	}
	
	public String getEmail (Context ctx, String mail)
	{
		Account[] accounts = AccountManager.get(this).getAccounts();
		String strGmail = null;
	    Log.e("", "Size: " + accounts.length);
	    for (Account account : accounts) {

	        String possibleEmail = account.name;
	        String type = account.type;

	        if (type.equals(mail)) {
	             strGmail = possibleEmail;
	            Log.e("", "Emails: " + strGmail);
	            break;
	        }
	
	    }
	    
	    return strGmail;
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radioMale:
	            if (checked)
	                gender = "M";
	            break;
	        case R.id.radioFemale:
	            if (checked)
	               gender = "F";
	            break;
	    }
	}
}