package br.com.caco.adapters;

import java.util.List;

import br.com.caco.R;
import br.com.caco.model.Friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsAdapter extends BaseAdapter {

	private Context context;
	private List<Friend> friendList;

	public FriendsAdapter(Context context, List<Friend> storeList) {
		this.context = context;
		this.friendList = storeList;
	}

	@Override
	public int getCount() {

		return friendList.size();
	}

	@Override
	public Object getItem(int position) {

		return friendList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return friendList.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend item = friendList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.list_friends_item, null);
		TextView textPersonName = (TextView) v
				.findViewById(R.id.textViewListFriendsPersonName);
		
		ImageView imageFriendPicture = (ImageView) v
				.findViewById(R.id.imageViewListFriendsPersonPicture);

		if (item.isAdd() == true) {
			ImageView imageViewAdd = (ImageView) v
					.findViewById(R.id.imageViewListFriendsPersonAdd);
			imageViewAdd.setImageResource(R.drawable.person);
		}
		else
		{
			ImageView imageViewAdd = (ImageView) v
					.findViewById(R.id.imageViewListFriendsPersonAdd);
			imageViewAdd.setImageResource(R.drawable.person_add);
		}

		textPersonName.setText(item.getName());
		imageFriendPicture.setImageBitmap(item.getBitmapImg());

		return v;
	}

}
