package br.com.caco.adapters;

import br.com.caco.fragment.GeolocationCardsFragment;
import br.com.caco.fragment.NotificationFragment;
import br.com.caco.fragment.RecentlyUsedCardsFragment;
import br.com.caco.fragment.StoresFragment;
import android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FidelityCardTabPagerAdapter extends FragmentPagerAdapter {

	public FidelityCardTabPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		// Bundle bundle = new Bundle();
		// String tab = "";
		Fragment tab = null;
		switch (index) {
		case 0:

			tab = new RecentlyUsedCardsFragment();
			break;
		case 1:

			tab = new GeolocationCardsFragment();
			break;

		case 2:

			tab = new StoresFragment();
			break;

		case 3:

			tab = new NotificationFragment();
			break;
		}
		// bundle.putString("tab",tab);
		// bundle.putInt("color", colorResId);

		return tab;

	}

	@Override
	public int getCount() {
		return 4;
	}
}