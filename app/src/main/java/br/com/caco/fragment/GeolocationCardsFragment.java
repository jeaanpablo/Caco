package br.com.caco.fragment;

import java.util.ArrayList;
import java.util.List;

import br.com.caco.R;
import br.com.caco.adapters.GeolocationFidelityCardListItemAdapter;
import br.com.caco.adapters.RecentlyUsedFidelityCardListItemAdapter;
import br.com.caco.gui.FidelityCardActivity;
import br.com.caco.model.FidelityCardListItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class GeolocationCardsFragment extends Fragment {

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
				R.layout.fragment_geolocation_fidelity_cards, null);

		List<FidelityCardListItem> list = new ArrayList<FidelityCardListItem>();

		for (int i = 0; i < 10; i++) {
			FidelityCardListItem item = new FidelityCardListItem("Mr. Kistch",
					"1500 pontos", R.drawable.mr_kistch, "10/12/2014", "56 km");
			list.add(item);
		}
		GeolocationFidelityCardListItemAdapter adapter = new GeolocationFidelityCardListItemAdapter(
				view.getContext(), list);
		ListView listView = (ListView) view
				.findViewById(R.id.listFidelityCardsGeolocation);
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
