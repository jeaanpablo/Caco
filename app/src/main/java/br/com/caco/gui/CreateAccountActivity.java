package br.com.caco.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

import br.com.caco.R;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.util.Mask;
import br.com.caco.util.Util;
import br.com.caco.util.Validation;
import br.com.caco.model.User;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class CreateAccountActivity extends Activity{
	String gender;
	private static final String TAG = "HttpClient";

    private TextWatcher cpfMask;
    private TextWatcher dateMask;
    @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		getActionBar().setTitle(R.string.login_criarconta);
		
		final EditText editEmail = (EditText) findViewById(R.id.editTextCreateAccountEmail);
        final EditText editCpf = (EditText) findViewById(R.id.editTextCreateAccountCpf);
        final EditText editAniversario = (EditText) findViewById(R.id.editTextCreateAccountAniversario);
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

        cpfMask = Mask.insert("###.###.###-##", editCpf);
        editCpf.addTextChangedListener(cpfMask);
        dateMask = Mask.insert("##/##/####", editAniversario);
        editAniversario.addTextChangedListener(dateMask);

		btnCriar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                boolean nome, celular, senha, login, email, cpfV, aniversario;
				final EditText editNome = (EditText) findViewById(R.id.editTextCreateAccountNome);
				EditText editCelular = (EditText) findViewById(R.id.editTextCreateAccountCelular);
				EditText editSenha = (EditText) findViewById(R.id.editTextCreateAccountSenha);
                EditText editLogin = (EditText) findViewById(R.id.editTextCreateAccountLogin);

                nome = Validation.hasText(editNome);
                senha = Validation.hasText(editSenha);
                login = Validation.hasText(editLogin);
                email = Validation.isEmailAddress(editEmail, true);
                celular = Validation.hasText(editCelular);
                cpfV = Validation.isValid(editCpf, "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}", "CPF invalido", true);
                aniversario = Validation.isValid(editAniversario, "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[12][0-9]{3}$", "Data invalida", true);

                if((nome == Boolean.TRUE) && (senha == Boolean.TRUE) && (login == Boolean.TRUE) && (email == Boolean.TRUE) && (celular == Boolean.TRUE) && (cpfV == Boolean.TRUE) && (aniversario == Boolean.TRUE)) {


                    User user = new User();
                    String name = editNome.getText().toString();

                    if (name.split("\\w+").length > 1) {

                        user.setLastName(name.substring(name.lastIndexOf(" ") + 1));
                        user.setFirstName(name.substring(0, name.lastIndexOf(' ')));
                    } else {
                        user.setFirstName(name);
                    }
                    user.setCellphone(Long.parseLong(editCelular.getText().toString()));
                    user.setEmail(editEmail.getText().toString());
                    user.setGender(gender);
                    user.setBirthdate(dateStringToMillis(editAniversario.getText().toString()));
                    user.setLogin(editLogin.getText().toString());
                    user.setPassword(editSenha.getText().toString());
                    String cpf = editCpf.getText().toString();

                    user.setCpf(Long.parseLong(cpf.replaceAll("[\\D]", "")));


                    createAccount(v.getContext(), user);


                }
				
			}
		});
	}

    public User postData(User user) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://45.79.178.168:8080/Caco-webservice/addUserApp");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("first_name", user.getFirstName()));
            nameValuePairs.add(new BasicNameValuePair("last_name", user.getLastName()));
            nameValuePairs.add(new BasicNameValuePair("cpf", "" + user.getCpf()));
            nameValuePairs.add(new BasicNameValuePair("cpf", ""+user.getCpf()));
            nameValuePairs.add(new BasicNameValuePair("birth_date", user.getBirthdate()));
            nameValuePairs.add(new BasicNameValuePair("gender", user.getGender()));
            nameValuePairs.add(new BasicNameValuePair("email", user.getEmail()));
            nameValuePairs.add(new BasicNameValuePair("login", user.getLogin()));
            nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
            nameValuePairs.add(new BasicNameValuePair("cellphone", ""+user.getCellphone()));


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

    public String dateStringToMillis(String date)
    {
        Calendar calendar = Calendar.getInstance();

        List<String> dateSplice = Util.fastSplit(date, '/', false);

        int dayOfMonth = Integer.parseInt(dateSplice.get(0));
        int month =  (Integer.parseInt(dateSplice.get(1))-1);
        int year = Integer.parseInt(dateSplice.get(2));

        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        String timeInMillis = ""+calendar.getTimeInMillis();

        return timeInMillis;
    }


    public void createAccount(final Context context, final User user)
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
                    Intent itMain = new Intent(context, FidelityCardActivity.class);
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


}