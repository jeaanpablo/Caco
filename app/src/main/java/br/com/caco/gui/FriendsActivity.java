package br.com.caco.gui;

import java.util.ArrayList;
import java.util.List;

import br.com.caco.R;
import br.com.caco.adapters.FriendsAdapter;
import br.com.caco.adapters.NotificationsAdapter;
import br.com.caco.model.Friend;
import br.com.caco.model.Notification;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.SearchView;

public class FriendsActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 handleIntent(getIntent());
		 setContentView(R.layout.activity_friends);
		 
			List<Friend> list = new ArrayList<Friend>();

			for (int i = 0; i < 10; i++) {
				Friend item = new Friend("Goku", true, R.drawable.goku);
				list.add(item);
			}
			FriendsAdapter adapter = new FriendsAdapter (getApplicationContext(), list);
			ListView listView = (ListView) findViewById(R.id.listFriends);
			listView.setAdapter(adapter);
		 
		 
		super.onCreate(savedInstanceState);
	}
	

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String k = "never show";
            //use the query to search your data somehow
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.friends_menu, menu);
	    // Associate searchable configuration with the SearchView
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
	    searchView.setSearchableInfo(
	            searchManager.getSearchableInfo(getComponentName()));

	    return true;
	}

}
