package br.com.caco.adapters;

import java.util.List;

import br.com.caco.R;
import br.com.caco.model.FidelityCardListItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GeolocationFidelityCardListItemAdapter extends BaseAdapter {

	private Context context;
	private List<FidelityCardListItem> storeList;

	public GeolocationFidelityCardListItemAdapter(Context context,
			List<FidelityCardListItem> storeList) {
		this.context = context;
		this.storeList = storeList;
	}

	@Override
	public int getCount() {

		return storeList.size();
	}

	@Override
	public Object getItem(int position) {

		return storeList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return storeList.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FidelityCardListItem item = storeList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(
				R.layout.list_geolocation_fidelity_card, null);
		TextView textStoreName = (TextView) v
				.findViewById(R.id.textViewListFidelyCardGeolocationName);
		TextView textStorePoints = (TextView) v
				.findViewById(R.id.textViewListFidelyCardGeolocationPoints);
		TextView textStoreDistance = (TextView) v
				.findViewById(R.id.textViewListFidelyCardDistance);
		ImageView imageStoreLogo = (ImageView) v
				.findViewById(R.id.imageViewListFidelityCardGeolocationStore);

		textStoreName.setText(item.getStoreName());
		textStorePoints.setText(item.getPoints());
		imageStoreLogo.setImageResource(item.getStoreLogo());
		textStoreDistance.setText(item.getDistance());

		return v;
	}

}
