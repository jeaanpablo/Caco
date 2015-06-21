package br.com.caco.adapters;

import java.util.List;

import br.com.caco.R;
import br.com.caco.model.FidelityCardListItem;
import br.com.caco.model.Store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllStoresAdapter extends BaseAdapter {

	private Context context;
	private List<Store> storeList;

	public AllStoresAdapter(Context context,
			List<Store> storeList) {
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
		Store item = storeList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater
				.inflate(R.layout.list_all_stores_item, null);
		TextView textStoreName = (TextView) v
				.findViewById(R.id.textViewAllStoresName);
		TextView textStoreAddress = (TextView) v
				.findViewById(R.id.textViewAllStoresAddress);
		ImageView imageStoreLogo = (ImageView) v
				.findViewById(R.id.imageViewListAllStores);
		
		if(item.isFidelity() == true)
		{
			ImageView imageViewFidelity = (ImageView) v
					.findViewById(R.id.imageViewCacoFidelity);
			imageViewFidelity.setImageResource(R.drawable.caco);
		}

		textStoreName.setText(item.getName());
		textStoreAddress.setText(item.getAddress());
		imageStoreLogo.setImageResource(item.getStoreLogo());

		return v;
	}

}
