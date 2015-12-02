package br.com.caco.adapters;

import java.util.List;

import br.com.caco.R;
import br.com.caco.model.LoyalityCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentlyUsedFidelityCardListItemAdapter extends BaseAdapter{
	
	private Context context;
	private List<LoyalityCard> storeList;
	
	public RecentlyUsedFidelityCardListItemAdapter(Context context, List<LoyalityCard> storeList)
	{
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
		LoyalityCard item = storeList.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.list_recently_used_fidelity_card_item, null);
		TextView textStoreName = (TextView) v.findViewById(R.id.textViewListFidelyCardName);
		TextView textStorePoints = (TextView) v.findViewById(R.id.textViewListFidelyCardPoints);
		TextView textStoreDate = (TextView) v.findViewById(R.id.textViewListFidelyCardDate);
		ImageView imageStoreLogo = (ImageView) v.findViewById(R.id.imageViewListFidelityCardStore);
		
		textStoreName.setText(item.getStoreName());
		textStorePoints.setText(item.getPoints() + " Pontos");
		imageStoreLogo.setImageBitmap(item.getStoreLogo());
		textStoreDate.setText(item.getData());
		
		return v;
	}

}
