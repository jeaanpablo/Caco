package br.com.caco.fragment;

import java.util.ArrayList;
import java.util.List;

import br.com.caco.R;
import br.com.caco.adapters.AllStoresAdapter;
import br.com.caco.adapters.GeolocationFidelityCardListItemAdapter;
import br.com.caco.adapters.RecentlyUsedFidelityCardListItemAdapter;
import br.com.caco.gui.FidelityCardActivity;
import br.com.caco.model.FidelityCardListItem;
import br.com.caco.model.Store;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class StoresFragment extends Fragment {

	private String tab;
	private int color;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				R.layout.fragment_all_stores, null);

		List<Store> list = new ArrayList<Store>();

		for (int i = 0; i < 10; i++) {
			Store item = new Store("Mr. Kistch","Avenida John Boyd Dunlop, 3900 - Jardim Ipaussurama, Campinas - SP", R.drawable.mr_kistch);
			
			if(i % 2 == 0)
			{
				item.setFidelity(true);
			}
			
			list.add(item);
		}
		AllStoresAdapter adapter = new AllStoresAdapter(
				view.getContext(), list);
		ListView listView = (ListView) view
				.findViewById(R.id.listAllStores);
		listView.setAdapter(adapter);

		// tv.setText(tab);
		// view.setBackgroundResource(color);
		return view;
	}

	@Override
	public void onResume() {
		((FidelityCardActivity) getActivity()).setTitle("Mais Próximos");
		super.onResume();
	}
}
