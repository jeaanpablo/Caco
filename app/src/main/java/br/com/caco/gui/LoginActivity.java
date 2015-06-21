package br.com.caco.gui;


import br.com.caco.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

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
				Intent itMain = new Intent (getApplicationContext(), FidelityCardActivity.class);
				startActivity(itMain);
				
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

}
