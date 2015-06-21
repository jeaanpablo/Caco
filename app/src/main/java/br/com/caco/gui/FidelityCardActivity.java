package br.com.caco.gui;

import br.com.caco.R;
import br.com.caco.adapters.FidelityCardTabPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class FidelityCardActivity extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private ActionBar actionBar;
    private FidelityCardTabPagerAdapter tabPagerAdapter;
    private String[] tabs = { "Recentily Used Cards", "Geolocation Cards"};
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

        
            actionBar.addTab(actionBar.newTab().setIcon(R.drawable.calendar).setTabListener(this));
            actionBar.addTab(actionBar.newTab().setIcon(R.drawable.geolocation).setTabListener(this));
            actionBar.addTab(actionBar.newTab().setIcon(R.drawable.store).setTabListener(this));
            actionBar.addTab(actionBar.newTab().setIcon(R.drawable.world).setTabListener(this));


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * on swipe select the respective tab
             * */
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageScrollStateChanged(int arg0) { }
        });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) { }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
        
        if(tab.getPosition() == 0)
        {
        	getActionBar().setTitle("Utilizados Recentemente");
        }
        
        if(tab.getPosition() == 1)
        {
        	getActionBar().setTitle("Mais Próximos");
        }
        
        if(tab.getPosition() == 2)
        {
        	getActionBar().setTitle("Lojas");
        }
        
        if(tab.getPosition() == 3)
        {
        	getActionBar().setTitle("Notificações");
        }
        

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
    
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
    
public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == R.id.action_profile)
		{		
			Intent itMain = new Intent (getApplicationContext(), ProfileActivity.class);
			startActivity(itMain);		
		}
		
		if(item.getItemId() == R.id.action_friends)
		{		
			Intent itMain = new Intent (getApplicationContext(), FriendsActivity.class);
			startActivity(itMain);		
		}
		
	
		return super.onOptionsItemSelected(item);
	}
    
}